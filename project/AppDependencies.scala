import sbt._

object AppDependencies {

  import play.core.PlayVersion

  val compile = Seq(
    play.sbt.PlayImport.ws,
    "uk.gov.hmrc" %% "bootstrap-frontend-play-27" % "3.0.0",
    "org.reactivemongo" %% "play2-reactivemongo" % "0.18.6-play26",
    "uk.gov.hmrc" %% "logback-json-logger" % "4.8.0",
    "uk.gov.hmrc" %% "govuk-template" % "5.60.0-play-27",
    "uk.gov.hmrc" %% "play-health" % "3.15.0-play-27",
    "uk.gov.hmrc" %% "play-ui" % "8.17.0-play-27",
    "uk.gov.hmrc" %% "play-conditional-form-mapping" % "1.4.0-play-26",
    "uk.gov.hmrc" %% "play-whitelist-filter" % "3.4.0-play-27",
    "uk.gov.hmrc" %% "play-frontend-govuk" % "0.54.0-play-27",
    "org.webjars.npm" % "govuk-frontend" % "3.9.1",
    "com.typesafe.play" %% "play-iteratees" % "2.6.1"
  )

  val test = Seq(
    "org.scalatest"             %%    "scalatest"                   % "3.0.8",
    "org.scalatestplus.play"    %%    "scalatestplus-play"          % "3.1.2",
    "org.pegdown"               %     "pegdown"                     % "1.6.0",
    "org.jsoup"                 %     "jsoup"                       % "1.12.1",
    "com.typesafe.play"         %%    "play-test"                   % PlayVersion.current,
    "org.mockito"               %     "mockito-all"                 % "1.10.19",
    "org.scalacheck"            %%    "scalacheck"                  % "1.14.1",
    "org.scalamock"             %%    "scalamock-scalatest-support" % "3.6.0",
    "com.github.tomakehurst"    %     "wiremock-standalone"         % "2.22.0",
    "org.jsoup"                 %     "jsoup"                       % "1.12.1"
  ).map(_ % "test, it")

  def apply(): Seq[ModuleID] = compile ++ test
}
