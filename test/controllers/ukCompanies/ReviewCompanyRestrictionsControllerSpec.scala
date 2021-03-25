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
import play.api.test.Helpers._
import views.html.ukCompanies.ReviewCompanyRestrictionsView
import navigation.FakeNavigators.FakeUkCompaniesNavigator
import models.{UserAnswers, CompanyDetailsModel}
import models.returnModels.fullReturn._
import utils.ReviewCompanyRestrictionsHelper
import pages.ukCompanies.UkCompaniesPage

class ReviewCompanyRestrictionsControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction {

  val view = injector.instanceOf[ReviewCompanyRestrictionsView]

  val companyIdx = 1
  val postAction = routes.ReviewCompanyRestrictionsController.onSubmit(companyIdx)
  val company = UkCompanyModel(CompanyDetailsModel("Company 1", "1123456789"))
  val userAnswers = UserAnswers("id").set(UkCompaniesPage, company, Some(1)).get

  object Controller extends ReviewCompanyRestrictionsController(
    messagesApi = messagesApi,
    navigator = FakeUkCompaniesNavigator,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    controllerComponents = messagesControllerComponents,
    view = view
  )

  "ReviewCompanyRestrictionsController" must {

    "return OK and the correct view for a GET" in {

      mockGetAnswers(Some(userAnswers))

      val result = Controller.onPageLoad(companyIdx)(fakeRequest)
      val rows = new ReviewCompanyRestrictionsHelper(1, userAnswers).rows

      status(result) mustEqual OK
      contentAsString(result) mustEqual view(rows, "Company 1", postAction)(fakeRequest, messages, frontendAppConfig).toString
    }

    "redirect to the next page" in {

      mockGetAnswers(Some(userAnswers))

      val result = Controller.onSubmit(companyIdx)(fakeRequest)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      mockGetAnswers(None)

      val result = Controller.onPageLoad(companyIdx)(fakeRequest)

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url
    }

  }
}
