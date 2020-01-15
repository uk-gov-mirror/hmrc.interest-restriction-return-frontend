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
import controllers.actions.{DataRequiredActionImpl, FakeDataRetrievalActionEmptyAnswers, FakeIdentifierAction}
import mocks.MockSessionRepository
import models.NormalMode
import navigation.FakeNavigators.{FakeAboutReportingCompanyNavigator, FakeStartReturnNavigator}
import nunjucks.{MockNunjucksRenderer, SavedReturnTemplate}
import nunjucks.viewmodels.SavedReturnViewModel
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.twirl.api.Html
import views.html.SavedReturnView

class SavedReturnControllerSpec extends SpecBase with MockNunjucksRenderer with FeatureSwitching with MockSessionRepository {

  val view = injector.instanceOf[SavedReturnView]

  def controller = new SavedReturnController(
    messagesApi = messagesApi,
    identify = FakeIdentifierAction,
    getData = FakeDataRetrievalActionEmptyAnswers,
    requireData = new DataRequiredActionImpl,
    controllerComponents = messagesControllerComponents,
    view = view,
    sessionRepository = mockSessionRepository,
    renderer = mockNunjucksRenderer,
    startReturnNavigator = FakeStartReturnNavigator,
    aboutReportingCompanyNavigator = FakeAboutReportingCompanyNavigator
  )

  "SavedReturn Controller" must {

    "for the onPageLoad() method" must {

      "When Nunjucks rendering is enabled" must {

        "return OK and the correct view for a GET" in {

          enable(UseNunjucks)

          mockRender(SavedReturnTemplate, Json.toJsObject(SavedReturnViewModel(savedTilDate)))(Html("Success"))

          val result = controller.onPageLoad(fakeRequest)

          status(result) mustBe OK
        }
      }

      "When Nunjucks rendering is disabled" must {

        "return OK and the correct view for a GET" in {

          disable(UseNunjucks)

          val result = controller.onPageLoad(fakeRequest)

          status(result) mustBe OK
        }
      }
    }
  }

  "for the nextUnansweredQuestion() method" must {

    "redirect to the next page" in {

      val result = controller.nextUnansweredPage(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.startReturn.routes.ReportingCompanyAppointedController.onPageLoad(NormalMode).url)
    }
  }

  "for the deleteAndStartAgain() method" must {

    "redirect to the IndexRoute and clear the user answers held" in {

      mockClear(result = true)

      val result = controller.deleteAndStartAgain(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }
  }
}
