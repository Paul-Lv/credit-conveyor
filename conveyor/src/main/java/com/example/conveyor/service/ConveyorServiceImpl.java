package com.example.conveyor.service;

import com.example.conveyor.dto.CreditDTO;
import com.example.conveyor.dto.PaymentScheduleElement;
import com.example.dto.LoanApplicationRequestDTO;
import com.example.dto.LoanOfferDTO;
import com.example.dto.ScoringDataDTO;
import com.example.validation.PrescoringValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class ConveyorServiceImpl implements ConveyorService {

    private static final BigDecimal BASE_RATE = new BigDecimal("20"); // Базовая ставка 20%

    @Override
    public List<LoanOfferDTO> generateOffers(LoanApplicationRequestDTO request) {
        log.info("Начало прескоринга для заявки: {}", request);

        // Прескоринг
        PrescoringValidator.validate(request);
        log.info("Прескоринг пройден успешно");

        // Генерация 4 офферов
        List<LoanOfferDTO> offers = new ArrayList<>();

        // 1. Без страховки, без зарплатного клиента
        offers.add(createOffer(request, false, false));

        // 2. Без страховки, с зарплатным клиентом
        offers.add(createOffer(request, false, true));

        // 3. Со страховкой, без зарплатного клиента
        offers.add(createOffer(request, true, false));

        // 4. Со страховкой и с зарплатным клиентом
        offers.add(createOffer(request, true, true));

        // Сортировка от худшего к лучшему (по возрастанию ставки)
        offers.sort(Comparator.comparing(LoanOfferDTO::getRate));

        log.info("Сгенерировано {} предложений", offers.size());
        return offers;
    }

    @Override
    public CreditDTO calculateCredit(ScoringDataDTO scoringData) {
        log.info("Начало скоринга для данных: {}", scoringData);

        // TODO: Реализовать полноценный скоринг и расчет кредита
        // Пока возвращаем заглушку для тестирования

        CreditDTO credit = new CreditDTO();
        credit.setAmount(scoringData.getAmount());
        credit.setTerm(scoringData.getTerm());
        credit.setRate(BASE_RATE);
        credit.setIsInsuranceEnabled(scoringData.getIsInsuranceEnabled());
        credit.setIsSalaryClient(scoringData.getIsSalaryClient());

        // Расчет ежемесячного платежа (аннуитет)
        BigDecimal monthlyPayment = calculateMonthlyPayment(
                scoringData.getAmount(),
                BASE_RATE,
                scoringData.getTerm()
        );
        credit.setMonthlyPayment(monthlyPayment);

        // ПСК (для простоты примем равной ставке)
        credit.setPsk(BASE_RATE);

        // Создаем тестовый график платежей
        List<PaymentScheduleElement> schedule = generateTestSchedule(
                scoringData.getAmount(),
                monthlyPayment,
                BASE_RATE,
                scoringData.getTerm()
        );
        credit.setPaymentSchedule(schedule);

        log.info("Скоринг завершен, кредит рассчитан");
        return credit;
    }


    private LoanOfferDTO createOffer(LoanApplicationRequestDTO request,
                                     boolean isInsuranceEnabled,
                                     boolean isSalaryClient) {
        LoanOfferDTO offer = new LoanOfferDTO();
        offer.setApplicationId(null); // Будет заполнено в МС deal
        offer.setRequestedAmount(request.getAmount());
        offer.setTerm(request.getTerm());
        offer.setIsInsuranceEnabled(isInsuranceEnabled);
        offer.setIsSalaryClient(isSalaryClient);

        // Расчет ставки
        BigDecimal rate = BASE_RATE;
        if (isInsuranceEnabled) {
            rate = rate.subtract(new BigDecimal("5")); // -5% за страховку
        }
        if (isSalaryClient) {
            rate = rate.subtract(new BigDecimal("1")); // -1% за зарплатного клиента
        }
        offer.setRate(rate);

        // Расчет общей суммы
        BigDecimal totalAmount = request.getAmount();
        if (isInsuranceEnabled) {
            BigDecimal insuranceCost = calculateInsurance(request.getAmount(), request.getTerm());
            totalAmount = totalAmount.add(insuranceCost);
        }
        offer.setTotalAmount(totalAmount);

        // Расчет ежемесячного платежа
        BigDecimal monthlyPayment = calculateMonthlyPayment(totalAmount, rate, request.getTerm());
        offer.setMonthlyPayment(monthlyPayment);

        return offer;
    }

    private BigDecimal calculateInsurance(BigDecimal amount, Integer term) {
        // Формула из задания: 10000 + (запрашиваемая_сумма/1000) * срок
        return new BigDecimal("10000")
                .add(amount.divide(new BigDecimal("1000"), RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(term)));
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, Integer term) {
        // Аннуитетный платеж: Платеж = Сумма * (Ставка_мес * (1 + Ставка_мес)^Срок) / ((1 + Ставка_мес)^Срок - 1)
        BigDecimal monthlyRate = rate.divide(new BigDecimal("1200"), 10, RoundingMode.HALF_UP);

        BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyRate);
        BigDecimal pow = onePlusRate.pow(term);

        BigDecimal numerator = monthlyRate.multiply(pow);
        BigDecimal denominator = pow.subtract(BigDecimal.ONE);

        return amount.multiply(numerator)
                .divide(denominator, 2, RoundingMode.HALF_UP);
    }

    private List<PaymentScheduleElement> generateTestSchedule(BigDecimal amount,
                                                              BigDecimal monthlyPayment,
                                                              BigDecimal rate,
                                                              Integer term) {
        List<PaymentScheduleElement> schedule = new ArrayList<>();
        BigDecimal remainingDebt = amount;
        BigDecimal monthlyRate = rate.divide(new BigDecimal("1200"), 10, RoundingMode.HALF_UP);

        LocalDate currentDate = LocalDate.now().plusMonths(1);

        for (int i = 1; i <= term; i++) {
            PaymentScheduleElement element = new PaymentScheduleElement();
            element.setNumber(i);
            element.setDate(currentDate);

            BigDecimal interestPayment = remainingDebt.multiply(monthlyRate)
                    .setScale(2, RoundingMode.HALF_UP);
            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment);

            element.setInterestPayment(interestPayment);
            element.setDebtPayment(debtPayment);
            element.setTotalPayment(monthlyPayment);

            remainingDebt = remainingDebt.subtract(debtPayment);
            element.setRemainingDebt(remainingDebt.max(BigDecimal.ZERO));

            schedule.add(element);
            currentDate = currentDate.plusMonths(1);
        }

        return schedule;
    }
}