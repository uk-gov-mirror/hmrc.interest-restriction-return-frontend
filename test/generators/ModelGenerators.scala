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

import models.InvestorRatioMethod.GroupRatioMethod
import models._
import models.returnModels.{CompanyNameModel, UTRModel}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

trait ModelGenerators {

  implicit lazy val arbitraryInvestorRatioMethod: Arbitrary[InvestorRatioMethod] =
    Arbitrary {
      Gen.oneOf(InvestorRatioMethod.values)
    }

  implicit lazy val arbitraryOtherInvestorGroupElections: Arbitrary[OtherInvestorGroupElections] =
    Arbitrary {
      Gen.oneOf(OtherInvestorGroupElections.values(GroupRatioMethod))
    }

  implicit lazy val arbitraryContinueSavedReturn: Arbitrary[ContinueSavedReturn] =
    Arbitrary {
      Gen.oneOf(ContinueSavedReturn.values)
    }

  implicit lazy val arbitraryFullOrAbbreviatedReturn: Arbitrary[FullOrAbbreviatedReturn] =
    Arbitrary {
      Gen.oneOf(FullOrAbbreviatedReturn.values)
    }

  implicit lazy val arbitraryCompanyDetailsModel: Arbitrary[CompanyDetailsModel] =
    Arbitrary {
      for {
        name <- arbitrary[String]
        utr <- arbitrary[String]
      } yield  CompanyDetailsModel(CompanyNameModel(name), UTRModel(utr))
    }
}
