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

package generators

import models._
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import pages._
import play.api.libs.json.{JsValue, Json}

trait UserAnswersEntryGenerators extends PageGenerators with ModelGenerators {

  implicit lazy val arbitraryGroupSubjectToReactivationsUserAnswersEntry: Arbitrary[(GroupSubjectToReactivationsPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[GroupSubjectToReactivationsPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryAgentNameUserAnswersEntry: Arbitrary[(AgentNamePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[AgentNamePage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryAgentActingOnBehalfOfCompanyUserAnswersEntry: Arbitrary[(AgentActingOnBehalfOfCompanyPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[AgentActingOnBehalfOfCompanyPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryHelloWorldYesNoUserAnswersEntry: Arbitrary[(HelloWorldYesNoPageNunjucks.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[HelloWorldYesNoPageNunjucks.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }
}
