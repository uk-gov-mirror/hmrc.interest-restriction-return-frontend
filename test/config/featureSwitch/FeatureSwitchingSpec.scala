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

package config.featureSwitch

import base.SpecBase

class FeatureSwitchingSpec extends SpecBase with FeatureSwitching {

  "FeatureSwitching" must {

    // Tests need adding in when we have at least 1 feature switch

    "Load config from appConfig if nothing set in sysProps" in {
//      sys.props -= {FeatureName}}.name
//      isEnabled({FeatureName}}) mustBe false
    }

    "Allow a feature switch to be enabled" in {
//      enable({FeatureName}})
//      sys.props.get({FeatureName}}.name) mustBe Some(FEATURE_SWITCH_ON)
    }

    "Allow a feature switch to be disabled" in {
//      disable({FeatureName}})
//      sys.props.get({FeatureName}}.name) mustBe Some(FEATURE_SWITCH_OFF)
    }

    "Allow checking of an enabled feature switch via a helper" in {
//      enable({FeatureName}})
//      isEnabled({FeatureName}}) mustBe true
    }

    "Allow checking of a disabled feature switch via a helper" in {
//      disable({FeatureName}})
//      isEnabled({FeatureName}}) mustBe false
    }
  }
}
