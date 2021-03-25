/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers.reviewAndComplete

import assets.constants.BaseConstants
import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import mocks.MockInterestRestrictionReturnConnector
import models.FullOrAbbreviatedReturn.Full
import models.UserAnswers
import models.returnModels.{AccountingPeriodModel, CompanyNameModel, DeemedParentModel, ReviewAndCompleteModel, UTRModel}
import navigation.FakeNavigators.FakeReviewAndCompleteNavigator
import pages.aboutReturn.{AccountingPeriodPage, AgentActingOnBehalfOfCompanyPage, AgentNamePage, FullOrAbbreviatedReturnPage, ReportingCompanyAppointedPage, ReportingCompanyCTUTRPage, ReportingCompanyNamePage, TellUsWhatHasChangedPage}
import pages.elections.{ElectedInterestAllowanceAlternativeCalcBeforePage, ElectedInterestAllowanceConsolidatedPshipBeforePage, GroupRatioElectionPage, InterestAllowanceConsolidatedPshipElectionPage, InterestAllowanceNonConsolidatedInvestmentsElectionPage}
import pages.groupLevelInformation.{DisallowedAmountPage, EnterANGIEPage, GroupInterestAllowancePage, GroupInterestCapacityPage, GroupSubjectToRestrictionsPage, InterestAllowanceBroughtForwardPage, ReturnContainEstimatesPage, RevisingReturnPage}
import pages.reviewAndComplete.ReviewAndCompletePage
import pages.ultimateParentCompany.{DeemedParentPage, HasDeemedParentPage, ReportingCompanySameAsParentPage}
import play.api.test.Helpers._
import views.html.reviewAndComplete.ReviewAndCompleteView

import java.time.LocalDate


class ReviewAndCompleteControllerSpec extends SpecBase with FeatureSwitching with BaseConstants with MockDataRetrievalAction with MockInterestRestrictionReturnConnector {

  val view = injector.instanceOf[ReviewAndCompleteView]


  object Controller extends ReviewAndCompleteController(
    messagesApi = messagesApi,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    controllerComponents = messagesControllerComponents,
    sessionRepository = mockSessionRepository,
    view = view,
    navigator = FakeReviewAndCompleteNavigator,
    interestRestrictionReturnConnector = mockConnector
  )

  "ReviewAndComplete Controller" must {
    "return OK and the correct view for a GET" in {
      mockGetAnswers(Some(emptyUserAnswers
        .set(ReviewAndCompletePage, ReviewAndCompleteModel()).get
      ))

      val result = Controller.onPageLoad()(fakeRequest)

      status(result) mustEqual OK
    }

    "redirect to the next page when submitted" in {
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
        .set(AgentNamePage, agentName).get
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

      mockGetAnswers(Some(userAnswers))
      mockSubmitReturn()

      val result = Controller.onSubmit()(fakeDataRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }
  }
}
