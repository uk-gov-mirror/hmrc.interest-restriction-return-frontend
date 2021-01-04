/*
 * Copyright 2021 HM Revenue & Customs
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

package forms.elections

import forms.behaviours.CheckboxFieldBehaviours
import models.InvestorRatioMethod.FixedRatioMethod
import models.OtherInvestorGroupElections
import play.api.data.FormError

class OtherInvestorGroupElectionsFormProviderSpec extends CheckboxFieldBehaviours {

  val form = new OtherInvestorGroupElectionsFormProvider()()

  ".value" must {

    val fieldName = "value"

    behave like checkboxField[OtherInvestorGroupElections](
      form,
      fieldName,
      validValues  = OtherInvestorGroupElections.values(FixedRatioMethod),
      invalidError = FormError(s"$fieldName[0]", "otherInvestorGroupElections.error.invalid")
    )
  }
}
