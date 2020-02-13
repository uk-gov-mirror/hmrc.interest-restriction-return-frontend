#!/bin/bash

echo ""
echo "Applying migration GroupEBITDA"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/elections.routes
echo "### GroupEBITDA Controller" >> ../conf/elections.routes
echo "### ----------------------------------------" >> ../conf/elections.routes
export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "GroupEBITDA" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.elections.GroupEBITDAController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/elections.routes
echo "POST       /$kebabClassName                          controllers.elections.GroupEBITDAController.onSubmit(mode: Mode = NormalMode)" >> ../conf/elections.routes
echo "GET        /$kebabClassName/change                   controllers.elections.GroupEBITDAController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/elections.routes
echo "POST       /$kebabClassName/change                   controllers.elections.GroupEBITDAController.onSubmit(mode: Mode = CheckMode)" >> ../conf/elections.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# GroupEBITDAPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "groupEBITDA.title = GroupEBITDA" >> ../conf/messages.en
echo "groupEBITDA.heading = GroupEBITDA" >> ../conf/messages.en
echo "groupEBITDA.label= Enter the group-EBITDA" >> ../conf/messages.en
echo "groupEBITDA.checkYourAnswersLabel = GroupEBITDA" >> ../conf/messages.en
echo "groupEBITDA.error.nonNumeric = Enter your groupEBITDA using numbers" >> ../conf/messages.en
echo "groupEBITDA.error.required = Enter your groupEBITDA" >> ../conf/messages.en
echo "groupEBITDA.error.invalidNumeric = The groupEBITDA must be valid decimal or whole number" >> ../conf/messages.en
echo "groupEBITDA.error.outOfRange = GroupEBITDA must be between {0} and {1}" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# GroupEBITDAPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "groupEBITDA.title = GroupEBITDA" >> ../conf/messages.cy
echo "groupEBITDA.heading = GroupEBITDA" >> ../conf/messages.cy
echo "groupEBITDA.label=Enter the group-EBITDA" >> ../conf/messages.cy
echo "groupEBITDA.checkYourAnswersLabel = GroupEBITDA" >> ../conf/messages.cy
echo "groupEBITDA.error.nonNumeric = Enter your groupEBITDA using numbers" >> ../conf/messages.cy
echo "groupEBITDA.error.required = Enter your groupEBITDA" >> ../conf/messages.cy
echo "groupEBITDA.error.invalidNumeric = The groupEBITDA must be valid decimal or whole number" >> ../conf/messages.cy
echo "groupEBITDA.error.outOfRange = GroupEBITDA must be between {0} and {1}" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGroupEBITDAUserAnswersEntry: Arbitrary[(GroupEBITDAPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[GroupEBITDAPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGroupEBITDAPage: Arbitrary[GroupEBITDAPage.type] =";\
    print "    Arbitrary(GroupEBITDAPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(GroupEBITDAPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def groupEBITDA: Option[SummaryListRow] = answer(GroupEBITDAPage, electionsRoutes.GroupEBITDAController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    GroupEBITDAPage.toString -> GroupEBITDAPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    GroupEBITDAPage.toString -> GroupEBITDAPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val groupEBITDA = \"Enter the group-EBITDA\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|elections\/${kebabClassName}|g" ../generated-it/controllers/elections/GroupEBITDAControllerISpec.scala

echo "Migration GroupEBITDA completed"
