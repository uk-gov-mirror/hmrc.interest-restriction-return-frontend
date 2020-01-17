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
import forms.ContinueSavedReturnFormProvider
import models.ContinueSavedReturn.{ContinueReturn, NewReturn}
import models.{ContinueSavedReturn, NormalMode}
import navigation.FakeNavigators.{FakeAboutReportingCompanyNavigator, FakeAboutReturnNavigator, FakeNavigator, FakeStartReturnNavigator}
import nunjucks.viewmodels.RadioOptionsViewModel
import nunjucks.{ContinueSavedReturnTemplate, MockNunjucksRenderer}
import pages.ContinueSavedReturnPage
import play.api.data.Form
import play.api.libs.json.{JsObject, Json}
import play.api.test.Helpers._
import play.twirl.api.Html
import uk.gov.hmrc.nunjucks.NunjucksSupport
import views.html.ContinueSavedReturnView

class ContinueSavedReturnControllerSpec extends SpecBase with MockNunjucksRenderer with NunjucksSupport with FeatureSwitching {

  val formProvider = new ContinueSavedReturnFormProvider()
  val view = injector.instanceOf[ContinueSavedReturnView]
  val form = formProvider()

  def controller(dataRetrieval: DataRetrievalAction = FakeDataRetrievalActionEmptyAnswers) = new ContinueSavedReturnController(
    messagesApi = messagesApi,
    sessionRepository = sessionRepository,
    navigator = FakeNavigator,
    identify = FakeIdentifierAction,
    getData = dataRetrieval,
    requireData = new DataRequiredActionImpl,
    formProvider = new ContinueSavedReturnFormProvider,
    controllerComponents = messagesControllerComponents,
    view = view,
    renderer = mockNunjucksRenderer,
    startReturnNavigator = FakeStartReturnNavigator,
    aboutReportingCompanyNavigator = FakeAboutReportingCompanyNavigator,
    aboutReturnNavigator = FakeAboutReturnNavigator
  )

  def viewContext(form: Form[ContinueSavedReturn]): JsObject = Json.toJsObject(RadioOptionsViewModel(
    ContinueSavedReturn.options(form),
    form,
    NormalMode
  ))

  "ContinueSavedReturn Controller" must {

    "If rendering using the Nunjucks templating engine" must {

      "return OK and the correct view for a GET" in {

        enable(UseNunjucks)

        mockRender(ContinueSavedReturnTemplate, viewContext(form))(Html("Success"))

        val result = controller(FakeDataRetrievalActionEmptyAnswers).onPageLoad()(fakeRequest)

        status(result) mustEqual OK
        contentAsString(result) mustEqual "Success"
      }
    }

    "If rendering using the Twirl templating engine" must {

      "return OK and the correct view for a GET" in {

        disable(UseNunjucks)

        val result = controller(FakeDataRetrievalActionEmptyAnswers).onPageLoad()(fakeRequest)

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString
      }
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = emptyUserAnswers.set(ContinueSavedReturnPage, ContinueSavedReturn.values.head).success.value

      val result = controller(FakeDataRetrievalActionGeneral(Some(userAnswers))).onPageLoad()(fakeRequest)

      status(result) mustBe OK
    }

    "for the onSubmit() method" must {

      "the ContinueReturn case is selected" must {

        "redirect to the SavedReturnController.nextUnansweredPage() method" in {

          val request = fakeRequest.withFormUrlEncodedBody(("value", ContinueReturn.toString))

          val result = controller().onSubmit()(request)

          status(result) mustBe SEE_OTHER
          redirectLocation(result) mustBe Some(controllers.routes.SavedReturnController.nextUnansweredPage().url)
        }
      }

      "the NewReturn case is selected" must {

        "redirect to the SavedReturnController.deleteAndStartAgain() method" in {

          val request = fakeRequest.withFormUrlEncodedBody(("value", NewReturn.toString))

          val result = controller().onSubmit()(request)

          status(result) mustBe SEE_OTHER
          redirectLocation(result) mustBe Some(controllers.routes.SavedReturnController.deleteAndStartAgain().url)
        }
      }
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))

      val result = controller().onSubmit()(request)

      status(result) mustEqual BAD_REQUEST
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      val result = controller(FakeDataRetrievalActionNone).onPageLoad()(fakeRequest)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", ContinueSavedReturn.values.head.toString))

      val result = controller(FakeDataRetrievalActionNone).onSubmit()(request)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url
    }
  }
}
