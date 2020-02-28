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

package controllers.checkTotals

import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import models.UserAnswers
import models.requests.DataRequest
import navigation.CheckTotalsNavigator
import play.api.libs.json.Json
import play.api.test.Helpers._
import utils.CheckTotalsHelper
import views.html.checkTotals.DerivedCompanyView

class DerivedCompanyControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction {

  val view = injector.instanceOf[DerivedCompanyView]
  val checkTotalsNavigator = injector.instanceOf[CheckTotalsNavigator]
  val checkTotalsHelper = injector.instanceOf[CheckTotalsHelper]

  object Controller extends DerivedCompanyController(
    messagesApi = messagesApi,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    controllerComponents = messagesControllerComponents,
    view = view,
    navigator = checkTotalsNavigator,
    checkTotalsHelper = checkTotalsHelper
  )

  "DerivedCompany Controller" must {

    "return OK and the correct view for a GET" in {

      val json = Json.obj(
        "ukCompanies" -> Json.arr(
          Json.obj(
            "companyDetails" -> Json.obj(
              "companyName" -> "chaz limited",
              "ctutr" -> "12345"
            ),
            "netTaxInterestExpense" -> "1.0",
            "netTaxInterestIncome" -> "1.0",
            "taxEBITDA" -> "1.0"
          )
        ))

      val data = UserAnswers(userAnswersId, json)

      mockGetAnswers(Some(data))

      val request = DataRequest(fakeRequest,"id", data)

      val result = Controller.onPageLoad(request)
      status(result) mustBe OK
    }

    "redirect to ukcompanies given no data" in {

      val data = emptyUserAnswers

      mockGetAnswers(Some(data))

      val request = DataRequest(fakeRequest,"id", data)

      val result = Controller.onPageLoad(request)
      status(result) mustBe SEE_OTHER
    }
  }

}
