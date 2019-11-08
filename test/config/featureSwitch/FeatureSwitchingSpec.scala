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

package config.featureSwitch

import base.SpecBase

class FeatureSwitchingSpec extends SpecBase with FeatureSwitching {

  "FeatureSwitching" must {

    "Load config from appConfig if nothing set in sysProps" in {
      sys.props -= WelshLanguage.name
      isEnabled(WelshLanguage) mustBe false
    }

    "Allow a feature switch to be enabled" in {
      enable(WelshLanguage)
      sys.props.get(WelshLanguage.name) mustBe Some(FEATURE_SWITCH_ON)
    }

    "Allow a feature switch to be disabled" in {
      disable(WelshLanguage)
      sys.props.get(WelshLanguage.name) mustBe Some(FEATURE_SWITCH_OFF)
    }

    "Allow checking of an enabled feature switch via a helper" in {
      enable(WelshLanguage)
      isEnabled(WelshLanguage) mustBe true
    }

    "Allow checking of a disabled feature switch via a helper" in {
      disable(WelshLanguage)
      isEnabled(WelshLanguage) mustBe false
    }
  }
}
