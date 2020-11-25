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

package config

import java.util.Base64

import com.google.inject.{Inject, Singleton}
import models.returnModels.CountryCodeModel
import play.api.Environment
import play.api.libs.json.{JsSuccess, Json}
import play.api.mvc.{Call, RequestHeader}
import uk.gov.hmrc.play.bootstrap.binders.RedirectUrl
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

@Singleton
class FrontendAppConfig @Inject()(val servicesConfig: ServicesConfig, environment: Environment) {

  lazy val host: String = servicesConfig.getString("host")

  private val contactHost = servicesConfig.getString("contact-frontend.host")
  private val contactFormServiceIdentifier = "irr"

  val analyticsToken: String = servicesConfig.getString(s"google-analytics.token")
  val analyticsHost: String = servicesConfig.getString(s"google-analytics.host")

  val reportAProblemPartialUrl = s"$contactHost/contact/problem_reports_ajax?service=$contactFormServiceIdentifier"
  val reportAProblemNonJSUrl = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"

  private def requestUri(implicit request: RequestHeader) = RedirectUrl(host + request.uri)
  def feedbackUrl(implicit request: RequestHeader): String =
    s"$contactHost/contact/beta-feedback?service=$contactFormServiceIdentifier&backUrl=$requestUri"

  private lazy val exitSurveyBaseUrl = servicesConfig.getString("feedback-frontend.host") + servicesConfig.getString("feedback-frontend.url")
  lazy val exitSurveyUrl = s"$exitSurveyBaseUrl/$contactFormServiceIdentifier"

  private def whitelistConfig(key: String): Seq[String] =
    Some(new String(Base64.getDecoder.decode(servicesConfig.getString(key)), "UTF-8"))
      .map(_.split(",")).getOrElse(Array.empty).toSeq

  lazy val whitelistEnabled: Boolean = servicesConfig.getBoolean("whitelist.enabled")
  lazy val whitelistedIps: Seq[String] = whitelistConfig("whitelist.allowedIps")
  lazy val whitelistExcludedPaths: Seq[Call] = whitelistConfig("whitelist.excludedPaths").map(path => Call("GET", path))
  lazy val shutterPage: String = servicesConfig.getString("whitelist.shutter-page-url")

  lazy val loginUrl: String = servicesConfig.getString("urls.login")
  lazy val signOutUrl: String = servicesConfig.getString("urls.signOut")
  lazy val loginContinueUrl: String = servicesConfig.getString("urls.loginContinue")
  lazy val findLostUTRUrl: String = servicesConfig.getString("urls.findLostUTR")

  lazy val timeout: Int = servicesConfig.getInt("timeout.timeout")
  lazy val countdown: Int = servicesConfig.getInt("timeout.countdown")

  lazy val dynamicStub = servicesConfig.baseUrl("interest-restriction-return-dynamic-stub")
  lazy val interestRestrictionReturn = servicesConfig.baseUrl("interest-restriction-return")

  lazy val cacheTtl = servicesConfig.getInt("mongodb.timeToLiveInSeconds")
  lazy val cacheTtlDays = cacheTtl / 24 / 3600

  lazy val countryCodes: Seq[CountryCodeModel] = environment.resourceAsStream("countryCodes.json").map(Json.parse) match {
    case Some(json) => json.validate[Seq[CountryCodeModel]] match {
      case JsSuccess(value, _) => value
      case _ => throw new IllegalArgumentException("Country code JSON does not match model")
    }
    case None => throw new IllegalArgumentException("Country code JSON resource missing")
  }

  lazy val countryCodeMap: Map[String, String] = countryCodes.map(x => x.code -> x.country).toMap
}
