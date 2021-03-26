package assets

import models.FullOrAbbreviatedReturn.Full
import models.UserAnswers
import models.returnModels.{AccountingPeriodModel, CRNModel, CompanyNameModel, CountryCodeModel, DeemedParentModel, UTRModel}
import pages.aboutReturn.{AccountingPeriodPage, AgentActingOnBehalfOfCompanyPage, AgentNamePage, FullOrAbbreviatedReturnPage, ReportingCompanyAppointedPage, ReportingCompanyCTUTRPage, ReportingCompanyNamePage, TellUsWhatHasChangedPage}
import pages.elections.{ElectedInterestAllowanceAlternativeCalcBeforePage, ElectedInterestAllowanceConsolidatedPshipBeforePage, GroupRatioElectionPage, InterestAllowanceConsolidatedPshipElectionPage, InterestAllowanceNonConsolidatedInvestmentsElectionPage}
import pages.groupLevelInformation.{DisallowedAmountPage, EnterANGIEPage, GroupInterestAllowancePage, GroupInterestCapacityPage, GroupSubjectToRestrictionsPage, InterestAllowanceBroughtForwardPage, ReturnContainEstimatesPage, RevisingReturnPage}
import pages.ultimateParentCompany.{DeemedParentPage, HasDeemedParentPage, ReportingCompanySameAsParentPage}

import java.time.LocalDate

trait BaseITConstants {

  val crn = "AA111111"
  val parentCompanyName = CompanyNameModel("Parent Company Ltd")
  val reportingCompanyName = CompanyNameModel("Reporting Company Ltd")
  val countryOfIncorporation = CountryCodeModel("AF", "Afghanistan")
  val ctutr = "1123456789"
  val ctutrModel = UTRModel(ctutr)
  val sautr = "5555555555"
  val sautrModel = UTRModel(sautr)
  val companyName = "A Company Name Ltd"
  val companyNameModel = CompanyNameModel(companyName)

  val date = LocalDate.of(2020,1,1)
  val deemedParentModel =
    DeemedParentModel(
      companyName = CompanyNameModel("Name"),
      sautr = Some(UTRModel("1123456789")),
      limitedLiabilityPartnership = Some(true),
      payTaxInUk = Some(true),
      reportingCompanySameAsParent = Some(false)
    )

  val userAnswers = UserAnswers("id")
    .set(ReportingCompanyAppointedPage, true).get
    .set(AgentActingOnBehalfOfCompanyPage, true).get
    .set(AgentNamePage, "Test").get
    .set(FullOrAbbreviatedReturnPage, Full).get
    .set(RevisingReturnPage, true).get
    .set(TellUsWhatHasChangedPage, "Something has changed").get
    .set(ReportingCompanyNamePage, companyNameModel.name).get
    .set(ReportingCompanyCTUTRPage, ctutrModel.utr).get
    .set(AccountingPeriodPage, AccountingPeriodModel(date, date.plusMonths(1))).get
    .set(GroupRatioElectionPage, false).get
    .set(GroupSubjectToRestrictionsPage, true).get
    .set(DisallowedAmountPage, BigDecimal(123.12)).get
    .set(InterestAllowanceBroughtForwardPage, BigDecimal(123.12)).get
    .set(GroupInterestAllowancePage, BigDecimal(123.12)).get
    .set(GroupInterestCapacityPage, BigDecimal(123.12)).get
    .set(EnterANGIEPage, BigDecimal(123.12)).get
    .set(ReturnContainEstimatesPage, false).get
    .set(ReportingCompanySameAsParentPage, false).get
    .set(HasDeemedParentPage, false).get
    .set(DeemedParentPage, deemedParentModel, Some(1)).get
    .set(GroupRatioElectionPage, false).get
    .set(ElectedInterestAllowanceAlternativeCalcBeforePage,true).get
    .set(InterestAllowanceNonConsolidatedInvestmentsElectionPage,false).get
    .set(ElectedInterestAllowanceConsolidatedPshipBeforePage,false).get
    .set(InterestAllowanceConsolidatedPshipElectionPage,false).get
}
