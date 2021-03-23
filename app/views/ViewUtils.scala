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

package views

import play.api.data.{Form, FormError}
import play.api.i18n.Messages

object ViewUtils {

  def title(form: Form[_], titleStr: String, section: Option[String] = None, titleMessageArgs: Seq[String] = Seq())
           (implicit messages: Messages): String = {
    titleNoForm(s"${errorPrefix(form)} ${messages(titleStr, titleMessageArgs: _*)}", section)
  }

  def titleNoForm(
                   title: String,
                   section: Option[String] = None,
                   titleMessageArgs: Seq[String] = Seq(),
                   sectionMessageArgs: Seq[String] = Seq()
                 )(implicit messages: Messages): String =
    s"${messages(title, titleMessageArgs: _*)} - ${section.fold("")(s => messages(s, sectionMessageArgs: _*) + " - ")}${messages("service.name")} - ${messages("site.govuk")}"

  def errorPrefix(form: Form[_])(implicit messages: Messages): String = {
    if (form.hasErrors || form.hasGlobalErrors) messages("error.browser.title.prefix") else ""
  }

  def addPossessive(name: String): String = {
    name match {
      case h if h.last.toLower == 's' => s"$h’"
      case _ => s"$name’s"
    }
  }

  def getErrorField(error: FormError, errorFieldName: Option[String], prefixToFieldMap: Map[String, String]): String =
    getErrorFieldFromPrefixes(error, prefixToFieldMap).getOrElse(
      errorFieldName.getOrElse(
        error.key
      )
    )

  def getErrorFieldFromPrefixes(error: FormError, prefixToFieldMap: Map[String, String]): Option[String] = {
    def predicate(prefixFieldTup: (String, String)): Boolean = {
      val (messagePrefix, errorField) = prefixFieldTup
      messagePrefix.r.findFirstIn(error.message).isDefined
    }

    prefixToFieldMap.find(predicate).map(_._2)
  }

}
