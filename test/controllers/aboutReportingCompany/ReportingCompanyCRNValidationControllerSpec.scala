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

package controllers.aboutReportingCompany

import assets.constants.BaseConstants
import base.SpecBase
import config.featureSwitch.{FeatureSwitching, UseNunjucks}
import connectors.httpParsers.{InvalidCRN, UnexpectedFailure, ValidCRN}
import controllers.actions._
import controllers.errors
import forms.aboutReportingCompany.ReportingCompanyCRNFormProvider
import models.NormalMode
import navigation.FakeNavigators.FakeAboutReportingCompanyNavigator
import nunjucks.viewmodels.BasicFormViewModel
import nunjucks.{MockNunjucksRenderer, ReportingCompanyCRNTemplate}
import pages.aboutReportingCompany.ReportingCompanyCRNPage
import play.api.data.Form
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.Call
import play.api.test.Helpers._
import play.twirl.api.Html
import services.mocks.MockCRNValidationService
import uk.gov.hmrc.nunjucks.NunjucksSupport
import views.html.aboutReportingCompany.ReportingCompanyCRNView
import play.api.http.Status



class ReportingCompanyCRNValidationControllerSpec extends SpecBase with NunjucksSupport with MockNunjucksRenderer
  with FeatureSwitching with MockCRNValidationService with BaseConstants {

  def onwardRoute = Call("GET", "/foo")

  val view = injector.instanceOf[ReportingCompanyCRNView]
  val formProvider = new ReportingCompanyCRNFormProvider()
  val form = formProvider()

  def controller(dataRetrieval: DataRetrievalAction = FakeDataRetrievalActionEmptyAnswers) = new ReportingCompanyCRNController(
    messagesApi = messagesApi,
    sessionRepository = sessionRepository,
    navigator = FakeAboutReportingCompanyNavigator,
    identify = FakeIdentifierAction,
    getData = dataRetrieval,
    requireData = new DataRequiredActionImpl,
    formProvider = new ReportingCompanyCRNFormProvider,
    controllerComponents = messagesControllerComponents,
    view = view,
    renderer = mockNunjucksRenderer,
    crnValidationService = mockCRNValidationService,
    errorHandler = errorHandler
  )

  def viewContext(form: Form[_]): JsObject = Json.toJsObject(BasicFormViewModel(form, NormalMode))

  "ReportingCompanyCRN Controller" must {

    "If rendering using the Nunjucks templating engine" must {

      "return OK and the correct view for a GET" in {

        enable(UseNunjucks)

        mockRender(ReportingCompanyCRNTemplate, viewContext(form))(Html("Success"))

        val result = controller(FakeDataRetrievalActionEmptyAnswers).onPageLoad(NormalMode)(fakeRequest)

        status(result) mustEqual OK
        contentAsString(result) mustEqual "Success"
      }
    }

    "If rendering using the Twirl templating engine" must {

      "return OK and the correct view for a GET" in {

        disable(UseNunjucks)

        val result = controller(FakeDataRetrievalActionEmptyAnswers).onPageLoad(NormalMode)(fakeRequest)

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString
      }
    }


    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = emptyUserAnswers.set(ReportingCompanyCRNPage, "answer").success.value

      val result = controller(FakeDataRetrievalActionGeneral(Some(userAnswers))).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustEqual OK
    }

    "redirect to the next page when valid data is submitted" in {

      mockValidateCRN(crnModel.crn)(Right(ValidCRN))

      val request = fakeRequest.withFormUrlEncodedBody(("value", crnModel.crn))

      val result = controller().onSubmit(NormalMode)(request)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some("/foo")
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", ""))

      val result = controller().onSubmit(NormalMode)(request)

      status(result) mustBe BAD_REQUEST
    }

    "return a Bad Request and errors when invalid crn is submitted" in {

      mockValidateCRN(crnModel.crn)(Left(InvalidCRN))

      val request = fakeRequest.withFormUrlEncodedBody(("value", crnModel.crn))

      val result = controller().onSubmit(NormalMode)(request)

      status(result) mustBe BAD_REQUEST
    }

    "return a Internal Server Error when Unexpected Failure is returned" in {

      mockValidateCRN(crnModel.crn)(Left(UnexpectedFailure(Status.INTERNAL_SERVER_ERROR, "Error")))

      val request = fakeRequest.withFormUrlEncodedBody(("value", crnModel.crn))

      val result = controller().onSubmit(NormalMode)(request)

      status(result) mustBe INTERNAL_SERVER_ERROR
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      val result = controller(FakeDataRetrievalActionNone).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(errors.routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "answer"))

      val result = controller(FakeDataRetrievalActionNone).onSubmit(NormalMode)(request)

      status(result) mustEqual SEE_OTHER

      redirectLocation(result) mustBe Some(errors.routes.SessionExpiredController.onPageLoad().url)
    }
  }
}
