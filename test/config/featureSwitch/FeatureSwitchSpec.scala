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

class FeatureSwitchSpec extends SpecBase {

  "FeatureSwitch" must {

    "for `.apply(fsName: String): FeatureSwitch`" must {

      "Construct a feature switch from a supplied key" in {
//      Needs to be added in when we have at least 1 feature switch.
//        FeatureSwitch("features.{feature-name}") mustBe {FeatureName}
      }

      "Throw Illegal Arg exception if feature switch does not exist" in {
        assertThrows[IllegalArgumentException] {
          FeatureSwitch("foo")
        }
      }
    }

    "for `.get(fsName: String): Option[FeatureSwitch]`" must {

      "Return Some({feature switch}) if a feature switch can be found" in {
//      Needs to be added in when we have at least 1 feature switch.
//        FeatureSwitch.get("features.{feature-name}") mustBe Some({feature-name})
      }

      "Return None if a feature switch can NOT be found" in {
        FeatureSwitch.get("foo") mustBe None
      }
    }
  }
}
