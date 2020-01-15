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

package controllers

import base.SpecBase
import config.featureSwitch.{FeatureSwitching, UseNunjucks}
import controllers.actions._
import forms.GroupInterestAllowanceFormProvider
import models.{NormalMode, UserAnswers}
import navigation.FakeNavigators.FakeNavigator
import nunjucks.{GroupInterestAllowanceTemplate, MockNunjucksRenderer}
import nunjucks.viewmodels.BasicFormViewModel
import pages.GroupInterestAllowancePage
import play.api.data.Form
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.Call
import play.api.test.Helpers._
import play.twirl.api.Html
import uk.gov.hmrc.nunjucks.NunjucksSupport
import views.html.GroupInterestAllowanceView

class GroupInterestAllowanceControllerSpec extends SpecBase with NunjucksSupport with FeatureSwitching with MockNunjucksRenderer {

  def onwardRoute = Call("GET", "/foo")

  val view = injector.instanceOf[GroupInterestAllowanceView]
  val formProvider = new GroupInterestAllowanceFormProvider()
  val form = formProvider()
  val validAnswer = 0

  def controller(dataRetrieval: DataRetrievalAction = FakeDataRetrievalActionEmptyAnswers) = new GroupInterestAllowanceController(
    messagesApi = messagesApi,
    sessionRepository = sessionRepository,
    navigator = FakeNavigator,
    identify = FakeIdentifierAction,
    getData = dataRetrieval,
    requireData = new DataRequiredActionImpl,
    formProvider = new GroupInterestAllowanceFormProvider,
    controllerComponents = messagesControllerComponents,
    view = view,
    mockNunjucksRenderer
  )

  def viewContext(form: Form[_]): JsObject = Json.toJsObject(BasicFormViewModel(form, NormalMode))

  "GroupInterestAllowance Controller" must {

    "If rendering using the Nunjucks templating engine" must {

      "return OK and the correct view for a GET" in {

        enable(UseNunjucks)

        mockRender(GroupInterestAllowanceTemplate, viewContext(form))(Html("Success"))

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

      val userAnswers = emptyUserAnswers.set[BigDecimal](GroupInterestAllowancePage, validAnswer).success.value

      val result = controller(FakeDataRetrievalActionGeneral(Some(userAnswers))).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
    }

    "redirect to the next page when valid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "01"))

      val result = controller().onSubmit(NormalMode)(request)

      status(result) mustBe SEE_OTHER

      redirectLocation(result) mustBe Some("/foo")
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "a"))

      val result = controller().onSubmit(NormalMode)(request)

      status(result) mustBe BAD_REQUEST
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      val result = controller(FakeDataRetrievalActionNone).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result).value mustBe errors.routes.SessionExpiredController.onPageLoad().url
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "2"))

      val result = controller(FakeDataRetrievalActionNone).onSubmit(NormalMode)(request)

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustBe errors.routes.SessionExpiredController.onPageLoad().url
    }
  }
}
