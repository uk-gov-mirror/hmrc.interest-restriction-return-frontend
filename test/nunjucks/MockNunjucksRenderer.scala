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

package nunjucks

import org.mockito.Matchers
import org.mockito.Mockito.when
import org.mockito.stubbing.OngoingStubbing
import org.scalatestplus.mockito.MockitoSugar
import play.api.libs.json.JsObject
import play.twirl.api.Html

import scala.concurrent.Future

trait MockNunjucksRenderer extends MockitoSugar {

  lazy val mockNunjucksRenderer: Renderer = mock[Renderer]

  def mockRender(template: String)(htmlResponse: Html): OngoingStubbing[Future[Html]] =
    when(mockNunjucksRenderer.render(Matchers.eq(template))(Matchers.any()))
      .thenReturn(Future.successful(htmlResponse))

  def mockRender(template: String, context: JsObject)(htmlResponse: Html): OngoingStubbing[Future[Html]] =
    when(mockNunjucksRenderer.render(Matchers.eq(template), Matchers.eq(context))(Matchers.any()))
    .thenReturn(Future.successful(htmlResponse))
}
