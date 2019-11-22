/*
 * Copyright 2019 HM Revenue & Customs
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
import forms.$className$FormProvider
import models.{$className$, NormalMode, UserAnswers}
import navigation.FakeNavigator
import org.scalatestplus.mockito.MockitoSugar
import pages.$className$Page
import nunjucks.{MockNunjucksRenderer, $className$Template}
import play.api.data.Form
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.Call
import play.api.test.Helpers._
import play.twirl.api.Html
import uk.gov.hmrc.viewmodels.Radios
import views.html.$className;format="decap"$View
import uk.gov.hmrc.nunjucks.NunjucksSupport
import nunjucks.viewmodels.RadioOptionsViewModel

class $className$ControllerSpec extends SpecBase with MockNunjucksRenderer with NunjucksSupport with FeatureSwitching {

  val formProvider = new $className$FormProvider()
  val view = injector.instanceOf[$className;format="decap"$View]
  val form = formProvider()

  def controller(dataRetrieval: DataRetrievalAction = FakeDataRetrievalActionEmptyAnswers) = new $className$Controller(
    messagesApi = messagesApi,
    sessionRepository = sessionRepository,
    navigator = FakeNavigator,
    identify = FakeIdentifierAction,
    getData = dataRetrieval,
    requireData = new DataRequiredActionImpl,
    formProvider = new $className$FormProvider,
    controllerComponents = messagesControllerComponents,
    view = view,
    renderer = mockNunjucksRenderer
  )

  def viewContext(form: Form[$className$]): JsObject = Json.toJsObject(RadioOptionsViewModel(
    $className$.options(form),
    form,
    NormalMode
  ))

  "$className$ Controller" must {

    "If rendering using the Nunjucks templating engine" must {

      "return OK and the correct view for a GET" in {

        enable(UseNunjucks)

        mockRender($className$Template, viewContext(form))(Html("Success"))

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

      val userAnswers = UserAnswers(userAnswersId).set($className$Page, $className$.values.head).success.value

      val result = controller(FakeDataRetrievalActionGeneral(Some(userAnswers))).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
    }

    "redirect to the next page when valid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", $className$.values.head.toString))

      val result = controller().onSubmit(NormalMode)(request)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustBe Some("/foo")
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))

      val result = controller().onSubmit(NormalMode)(request)

      status(result) mustEqual BAD_REQUEST
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      val result = controller(FakeDataRetrievalActionNone).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", $className$.values.head.toString))

      val result = controller(FakeDataRetrievalActionNone).onSubmit(NormalMode)(request)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url
    }
  }
}
