#!/bin/bash

echo ""
echo "Applying migration TellUsWhatHasChanged"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/aboutReturn.routes
echo "### TellUsWhatHasChanged Controller" >> ../conf/aboutReturn.routes
echo "### ----------------------------------------" >> ../conf/aboutReturn.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "TellUsWhatHasChanged" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.aboutReturn.TellUsWhatHasChangedController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/aboutReturn.routes
echo "POST       /$kebabClassName                          controllers.aboutReturn.TellUsWhatHasChangedController.onSubmit(mode: Mode = NormalMode)" >> ../conf/aboutReturn.routes
echo "GET        /$kebabClassName/change                   controllers.aboutReturn.TellUsWhatHasChangedController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/aboutReturn.routes
echo "POST       /$kebabClassName/change                   controllers.aboutReturn.TellUsWhatHasChangedController.onSubmit(mode: Mode = CheckMode)" >> ../conf/aboutReturn.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# TellUsWhatHasChangedPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "tellUsWhatHasChanged.title = tellUsWhatHasChanged" >> ../conf/messages.en
echo "tellUsWhatHasChanged.heading = tellUsWhatHasChanged" >> ../conf/messages.en
echo "tellUsWhatHasChanged.checkYourAnswersLabel = tellUsWhatHasChanged" >> ../conf/messages.en
echo "tellUsWhatHasChanged.label= Tell us what has changed" >> ../conf/messages.en
echo "tellUsWhatHasChanged.error.required = Enter tellUsWhatHasChanged" >> ../conf/messages.en
echo "tellUsWhatHasChanged.error.length = TellUsWhatHasChanged must be 5000 characters or less" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# TellUsWhatHasChangedPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "tellUsWhatHasChanged.title = tellUsWhatHasChanged" >> ../conf/messages.cy
echo "tellUsWhatHasChanged.heading = tellUsWhatHasChanged" >> ../conf/messages.cy
echo "tellUsWhatHasChanged.checkYourAnswersLabel = tellUsWhatHasChanged" >> ../conf/messages.cy
echo "tellUsWhatHasChanged.label = Tell us what has changed" >> ../conf/messages.cy
echo "tellUsWhatHasChanged.error.required = Enter tellUsWhatHasChanged" >> ../conf/messages.cy
echo "tellUsWhatHasChanged.error.length = TellUsWhatHasChanged must be 5000 characters or less" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryTellUsWhatHasChangedUserAnswersEntry: Arbitrary[(TellUsWhatHasChangedPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[TellUsWhatHasChangedPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryTellUsWhatHasChangedPage: Arbitrary[TellUsWhatHasChangedPage.type] =";\
    print "    Arbitrary(TellUsWhatHasChangedPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(TellUsWhatHasChangedPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def tellUsWhatHasChanged: Option[SummaryListRow] = answer(TellUsWhatHasChangedPage, aboutReturnRoutes.TellUsWhatHasChangedController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    TellUsWhatHasChangedPage.toString -> TellUsWhatHasChangedPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    TellUsWhatHasChangedPage.toString -> TellUsWhatHasChangedPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val tellUsWhatHasChanged = \"Tell us what has changed\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|aboutReturn\/${kebabClassName}|g" ../generated-it/controllers/aboutReturn/TellUsWhatHasChangedControllerISpec.scala

echo "Migration TellUsWhatHasChanged completed"
