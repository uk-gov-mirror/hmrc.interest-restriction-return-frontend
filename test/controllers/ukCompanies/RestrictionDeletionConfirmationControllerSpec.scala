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

package controllers.ukCompanies

import controllers.errors
import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import forms.ukCompanies.RestrictionDeletionConfirmationFormProvider
import models.{UserAnswers, NormalMode}
import play.api.test.Helpers._
import views.html.ukCompanies.RestrictionDeletionConfirmationView
import navigation.FakeNavigators.FakeUkCompaniesNavigator
import java.time.LocalDate
import scala.concurrent.Future
import pages.ukCompanies.{UkCompaniesPage, CompanyAccountingPeriodEndDatePage, AddRestrictionAmountPage, RestrictionAmountForAccountingPeriodPage}
import assets.constants.fullReturn.UkCompanyConstants.ukCompanyModelMax

class RestrictionDeletionConfirmationControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction {

  val view = injector.instanceOf[RestrictionDeletionConfirmationView]
  val formProvider = injector.instanceOf[RestrictionDeletionConfirmationFormProvider]
  val form = formProvider()

  object Controller extends RestrictionDeletionConfirmationController(
    messagesApi = messagesApi,
    sessionRepository = mockSessionRepository,
    navigator = FakeUkCompaniesNavigator,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    view = view
  )

  val companyIdx = 1
  val restrictionIdx = 1
  val postAction = routes.RestrictionDeletionConfirmationController.onSubmit(companyIdx, restrictionIdx, NormalMode)

  "RestrictionDeletionConfirmation Controller" must {

    "return OK and the correct view for a GET" in {

      mockGetAnswers(Some(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get))

      val result = Controller.onPageLoad(companyIdx, restrictionIdx, NormalMode)(fakeRequest)

      status(result) mustEqual OK
      contentAsString(result) mustEqual view(form, "Company Name ltd", postAction)(fakeRequest, messages, frontendAppConfig).toString
    }

    "redirect to the next page when valid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      mockGetAnswers(Some(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get))
      mockSetAnswers

      val result = Controller.onSubmit(companyIdx, restrictionIdx, NormalMode)(request)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "clear this restriction when true is entered" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "true"))
      val companyIdx = 1
      val restrictionIdx = 2

      val userAnswers = (for {
        ua1 <- emptyUserAnswers.set(CompanyAccountingPeriodEndDatePage(1, 1), LocalDate.of(2020,1,1))
        ua2 <- ua1.set(AddRestrictionAmountPage(1, 1), true)
        ua3 <- ua2.set(RestrictionAmountForAccountingPeriodPage(1, 1), BigDecimal(1))
        ua4 <- ua3.set(CompanyAccountingPeriodEndDatePage(1, 2), LocalDate.of(2020,1,1))
        ua5 <- ua4.set(AddRestrictionAmountPage(1, 2), true)
        ua6 <- ua5.set(RestrictionAmountForAccountingPeriodPage(1, 2), BigDecimal(1))
        ua7 <- ua6.set(CompanyAccountingPeriodEndDatePage(2, 1), LocalDate.of(2020,1,1))
        ua8 <- ua7.set(AddRestrictionAmountPage(2, 1), true)
        ua9 <- ua8.set(RestrictionAmountForAccountingPeriodPage(2, 1), BigDecimal(1))
        ua10 <- ua9.set(UkCompaniesPage, ukCompanyModelMax, Some(1))
      } yield ua10).get

      val expectedUserAnswers = (for {
        ua1 <- emptyUserAnswers.set(CompanyAccountingPeriodEndDatePage(1, 1), LocalDate.of(2020,1,1))
        ua2 <- ua1.set(AddRestrictionAmountPage(1, 1), true)
        ua3 <- ua2.set(RestrictionAmountForAccountingPeriodPage(1, 1), BigDecimal(1))
        ua4 <- ua3.set(CompanyAccountingPeriodEndDatePage(2, 1), LocalDate.of(2020,1,1))
        ua5 <- ua4.set(AddRestrictionAmountPage(2, 1), true)
        ua6 <- ua5.set(RestrictionAmountForAccountingPeriodPage(2, 1), BigDecimal(1))
        ua7 <- ua6.set(UkCompaniesPage, ukCompanyModelMax, Some(1))
      } yield ua7).get

      mockGetAnswers(Some(userAnswers))
     (mockSessionRepository.set(_: UserAnswers))
        .expects(expectedUserAnswers)
        .returns(Future.successful(true))

      val result = Controller.onSubmit(companyIdx, restrictionIdx, NormalMode)(request)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "do not clear this restriction when false is entered" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "false"))
      val companyIdx = 1
      val restrictionIdx = 2

      val userAnswers = (for {
        ua1 <- emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(1))
        ua2 <- ua1.set(CompanyAccountingPeriodEndDatePage(1, 1), LocalDate.of(2020,1,1))
        ua3 <- ua2.set(AddRestrictionAmountPage(1, 1), true)
        ua4 <- ua3.set(RestrictionAmountForAccountingPeriodPage(1, 1), BigDecimal(1))
        ua5 <- ua4.set(CompanyAccountingPeriodEndDatePage(1, 2), LocalDate.of(2020,1,1))
        ua6 <- ua5.set(AddRestrictionAmountPage(1, 2), true)
        ua7 <- ua6.set(RestrictionAmountForAccountingPeriodPage(1, 2), BigDecimal(1))
        ua8 <- ua7.set(CompanyAccountingPeriodEndDatePage(2, 1), LocalDate.of(2020,1,1))
        ua9 <- ua8.set(AddRestrictionAmountPage(2, 1), true)
        ua10 <- ua9.set(RestrictionAmountForAccountingPeriodPage(2, 1), BigDecimal(1))
      } yield ua10).get

      mockGetAnswers(Some(userAnswers))

      val result = Controller.onSubmit(companyIdx, restrictionIdx, NormalMode)(request)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "clearing a restriction with higher indexes retains the others and reduces their indexes" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "true"))
      val companyIdx = 1
      val restrictionIdx = 1

      val userAnswers = (for {
        ua1 <- emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(1))
        ua2 <- ua1.set(CompanyAccountingPeriodEndDatePage(1, 1), LocalDate.of(2021,1,1))
        ua3 <- ua2.set(AddRestrictionAmountPage(1, 1), true)
        ua4 <- ua3.set(RestrictionAmountForAccountingPeriodPage(1, 1), BigDecimal(1))

        ua5 <- ua4.set(CompanyAccountingPeriodEndDatePage(1, 2), LocalDate.of(2022,2,2))
        ua6 <- ua5.set(AddRestrictionAmountPage(1, 2), false)
        ua7 <- ua6.set(RestrictionAmountForAccountingPeriodPage(1, 2), BigDecimal(2))

        ua8 <- ua7.set(CompanyAccountingPeriodEndDatePage(1, 3), LocalDate.of(2023,3,3))
        ua9 <- ua8.set(AddRestrictionAmountPage(1, 3), true)
        ua10 <- ua9.set(RestrictionAmountForAccountingPeriodPage(1, 3), BigDecimal(3))
      } yield ua10).get

      val expectedUserAnswers = (for {
        ua1 <- emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(1))
        ua2 <- ua1.set(CompanyAccountingPeriodEndDatePage(1, 1), LocalDate.of(2022,2,2))
        ua3 <- ua2.set(AddRestrictionAmountPage(1, 1), false)
        ua4 <- ua3.set(RestrictionAmountForAccountingPeriodPage(1, 1), BigDecimal(2))
        ua5 <- ua4.set(CompanyAccountingPeriodEndDatePage(1, 2), LocalDate.of(2023,3,3))
        ua6 <- ua5.set(AddRestrictionAmountPage(1, 2), true)
        ua7 <- ua6.set(RestrictionAmountForAccountingPeriodPage(1, 2), BigDecimal(3))
      } yield ua7).get

      mockGetAnswers(Some(userAnswers))
     (mockSessionRepository.set(_: UserAnswers))
        .expects(expectedUserAnswers)
        .returns(Future.successful(true))

      val result = Controller.onSubmit(companyIdx, restrictionIdx, NormalMode)(request)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", ""))

      mockGetAnswers(Some(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get))

      val result = Controller.onSubmit(companyIdx, restrictionIdx, NormalMode)(request)

      status(result) mustEqual BAD_REQUEST
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      mockGetAnswers(None)

      val result = Controller.onPageLoad(companyIdx, restrictionIdx, NormalMode)(fakeRequest)

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      mockGetAnswers(None)

      val result = Controller.onSubmit(companyIdx, restrictionIdx, NormalMode)(request)

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url
    }
  }

}
