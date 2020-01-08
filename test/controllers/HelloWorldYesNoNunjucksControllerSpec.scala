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
import controllers.actions._
import forms.HelloWorldYesNoFormProvider
import models.{NormalMode, UserAnswers}
import navigation.FakeNavigator
import nunjucks.MockNunjucksRenderer
import nunjucks.viewmodels.YesNoRadioViewModel
import pages.HelloWorldYesNoPageNunjucks
import play.api.data.{FieldMapping, Form, FormError}
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.Call
import play.api.test.Helpers._
import play.twirl.api.Html
import uk.gov.hmrc.nunjucks.NunjucksSupport
import uk.gov.hmrc.viewmodels.Radios
import views.html.HelloWorldYesNoView

class HelloWorldYesNoNunjucksControllerSpec extends SpecBase with MockNunjucksRenderer with NunjucksSupport {

  val view = injector.instanceOf[HelloWorldYesNoView]
  val formProvider = injector.instanceOf[HelloWorldYesNoFormProvider]
  val form = formProvider()

  def controller(dataRetrieval: DataRetrievalAction = FakeDataRetrievalActionEmptyAnswers) = new HelloWorldYesNoNunjucksController(
    messagesApi = messagesApi,
    sessionRepository = sessionRepository,
    navigator = FakeNavigator,
    identify = FakeIdentifierAction,
    getData = dataRetrieval,
    requireData = injector.instanceOf[DataRequiredActionImpl],
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    renderer = mockNunjucksRenderer
  )

  def viewContext(form: Form[Boolean]): JsObject = Json.toJsObject(YesNoRadioViewModel(form, NormalMode))

  "HelloWorldYesNoNunjucks Controller" must {

    "return OK and the correct view for a GET" in {

      mockRender(nunjucks.HelloWorldYesNoTemplate, viewContext(form))(Html("Success"))

      val result = controller(FakeDataRetrievalActionEmptyAnswers).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustEqual OK
      contentAsString(result) mustBe "Success"
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      mockRender(nunjucks.HelloWorldYesNoTemplate, viewContext(form.fill(true)))(Html("Success"))

      val userAnswers = UserAnswers(userAnswersId).set(HelloWorldYesNoPageNunjucks, true).success.value

      val result = controller(FakeDataRetrievalActionGeneral(Some(userAnswers))).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustEqual OK
      contentAsString(result) mustBe "Success"
    }

    "redirect to the next page when valid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      val result = controller().onSubmit(NormalMode)(request)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustBe Some("/foo")
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      mockRender(nunjucks.HelloWorldYesNoTemplate, viewContext(form.bind(Map("value" -> ""))))(Html("Success"))

      val request = fakeRequest.withFormUrlEncodedBody(("value", ""))

      val result = controller().onSubmit(NormalMode)(request)

      status(result) mustEqual BAD_REQUEST
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      val result = controller(FakeDataRetrievalActionNone).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      val result = controller(FakeDataRetrievalActionNone).onSubmit(NormalMode)(request)

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url
    }
  }
}
