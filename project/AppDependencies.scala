import sbt._

object AppDependencies {
  import play.core.PlayVersion

  val compile = Seq(
    play.sbt.PlayImport.ws,
    "org.reactivemongo" %% "play2-reactivemongo"            % "0.18.3-play26",
    "uk.gov.hmrc"       %% "logback-json-logger"            % "4.6.0",
    "uk.gov.hmrc"       %% "govuk-template"                 % "5.36.0-play-26",
    "uk.gov.hmrc"       %% "play-health"                    % "3.14.0-play-26",
    "uk.gov.hmrc"       %% "play-ui"                        % "8.3.0-play-26",
    "uk.gov.hmrc"       %% "play-conditional-form-mapping"  % "1.2.0-play-26",
    "uk.gov.hmrc"       %% "bootstrap-play-26"              % "1.1.0",
    "uk.gov.hmrc"       %% "play-whitelist-filter"          % "3.1.0-play-26",
    "uk.gov.hmrc"       %% "play-frontend-govuk"            % "0.20.0-play-26",
    "uk.gov.hmrc"       %% "play-nunjucks"                  % "0.15.0-play-26",
    "uk.gov.hmrc"       %% "play-nunjucks-viewmodel"        % "0.2.0",
    "org.webjars.npm"    % "govuk-frontend"                 % "3.3.0"
  )

  val test = Seq(
    "org.scalatest"               %% "scalatest"                    % "3.0.7",
    "org.scalatestplus.play"      %% "scalatestplus-play"           % "3.1.2",
    "org.pegdown"                 %  "pegdown"                      % "1.6.0",
    "org.jsoup"                   %  "jsoup"                        % "1.10.3",
    "com.typesafe.play"           %% "play-test"                    % PlayVersion.current,
    "org.mockito"                 %  "mockito-all"                  % "1.10.19",
    "org.scalacheck"              %% "scalacheck"                   % "1.14.0",
    "org.scalamock"               %% "scalamock-scalatest-support"  % "3.6.0"
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test
}
