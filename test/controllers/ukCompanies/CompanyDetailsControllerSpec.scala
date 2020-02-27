/*
 * Copyright 2020 HM Revenue & Customs
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

import assets.constants.BaseConstants
import controllers.errors
import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import forms.ukCompanies.CompanyDetailsFormProvider
import models.{CompanyDetailsModel, NormalMode}
import pages.ukCompanies.CompanyDetailsPage
import play.api.test.Helpers._
import views.html.ukCompanies.CompanyDetailsView
import navigation.FakeNavigators.FakeUkCompaniesNavigator
import assets.constants.fullReturn.UkCompanyConstants.companyDetailsModel
import play.api.data.Form


class CompanyDetailsControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction with BaseConstants {

  val view: CompanyDetailsView = injector.instanceOf[CompanyDetailsView]
  val formProvider: CompanyDetailsFormProvider = injector.instanceOf[CompanyDetailsFormProvider]
  val form: Form[CompanyDetailsModel] = formProvider()

  object Controller extends CompanyDetailsController(
    messagesApi = messagesApi,
    sessionRepository = sessionRepository,
    navigator = FakeUkCompaniesNavigator,
    questionDeletionLookupService = questionDeletionLookupService,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    view = view
  )

  "CompanyDetailsModel Controller" must {

    "return OK and the correct view for a GET" in {

      mockGetAnswers(Some(emptyUserAnswers))

      val result = Controller.onPageLoad(1, NormalMode)(fakeRequest)

      status(result) mustEqual OK
      contentAsString(result) mustEqual view(
        form = form,
        mode = NormalMode,
        postAction = routes.CompanyDetailsController.onSubmit(1, NormalMode)
      )(fakeRequest, messages, frontendAppConfig).toString
    }


    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = emptyUserAnswers
        .set(CompanyDetailsPage, companyDetailsModel, Some(1)).success.value

      mockGetAnswers(Some(userAnswers))

      val result = Controller.onPageLoad(1, NormalMode)(fakeRequest)

      status(result) mustEqual OK
    }

    "redirect to the next page when valid data is submitted" in {

      mockGetAnswers(Some(emptyUserAnswers))

      val request = fakeRequest.withFormUrlEncodedBody(
        "companyNameValue" -> companyNameModel.name,
        "ctutrValue" -> ctutrModel.utr
      )
      println(companyDetailsModel)


      val result = Controller.onSubmit(1, NormalMode)(request)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", ""))

      mockGetAnswers(Some(emptyUserAnswers))

      val result = Controller.onSubmit(1, NormalMode)(request)

      status(result) mustBe BAD_REQUEST
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      mockGetAnswers(None)

      val result = Controller.onPageLoad(1, NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(errors.routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "answer"))

      mockGetAnswers(None)

      val result = Controller.onSubmit(1, NormalMode)(request)

      status(result) mustEqual SEE_OTHER

      redirectLocation(result) mustBe Some(errors.routes.SessionExpiredController.onPageLoad().url)
    }
  }
}
