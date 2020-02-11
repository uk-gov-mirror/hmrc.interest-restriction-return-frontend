#!/bin/bash

echo ""
echo "Applying migration GroupRatioPercentage"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "### GroupRatioPercentage Controller" >> ../conf/app.routes
echo "### ----------------------------------------" >> ../conf/app.routes
export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "GroupRatioPercentage" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.elections.GroupRatioPercentageController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName                          controllers.elections.GroupRatioPercentageController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "GET        /$kebabClassName/change                   controllers.elections.GroupRatioPercentageController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName/change                   controllers.elections.GroupRatioPercentageController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# GroupRatioPercentagePage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "groupRatioPercentage.title = GroupRatioPercentage" >> ../conf/messages.en
echo "groupRatioPercentage.heading = GroupRatioPercentage" >> ../conf/messages.en
echo "groupRatioPercentage.label= Enter the group ratio percentage" >> ../conf/messages.en
echo "groupRatioPercentage.checkYourAnswersLabel = GroupRatioPercentage" >> ../conf/messages.en
echo "groupRatioPercentage.error.nonNumeric = Enter your groupRatioPercentage using numbers" >> ../conf/messages.en
echo "groupRatioPercentage.error.required = Enter your groupRatioPercentage" >> ../conf/messages.en
echo "groupRatioPercentage.error.invalidNumeric = The groupRatioPercentage must be valid decimal or whole number" >> ../conf/messages.en
echo "groupRatioPercentage.error.outOfRange = GroupRatioPercentage must be between {0} and {1}" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# GroupRatioPercentagePage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "groupRatioPercentage.title = GroupRatioPercentage" >> ../conf/messages.cy
echo "groupRatioPercentage.heading = GroupRatioPercentage" >> ../conf/messages.cy
echo "groupRatioPercentage.label=Enter the group ratio percentage" >> ../conf/messages.cy
echo "groupRatioPercentage.checkYourAnswersLabel = GroupRatioPercentage" >> ../conf/messages.cy
echo "groupRatioPercentage.error.nonNumeric = Enter your groupRatioPercentage using numbers" >> ../conf/messages.cy
echo "groupRatioPercentage.error.required = Enter your groupRatioPercentage" >> ../conf/messages.cy
echo "groupRatioPercentage.error.invalidNumeric = The groupRatioPercentage must be valid decimal or whole number" >> ../conf/messages.cy
echo "groupRatioPercentage.error.outOfRange = GroupRatioPercentage must be between {0} and {1}" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGroupRatioPercentageUserAnswersEntry: Arbitrary[(GroupRatioPercentagePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[GroupRatioPercentagePage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGroupRatioPercentagePage: Arbitrary[GroupRatioPercentagePage.type] =";\
    print "    Arbitrary(GroupRatioPercentagePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(GroupRatioPercentagePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def groupRatioPercentage: Option[SummaryListRow] = answer(GroupRatioPercentagePage, routes.GroupRatioPercentageController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    GroupRatioPercentagePage.toString -> GroupRatioPercentagePage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    GroupRatioPercentagePage.toString -> GroupRatioPercentagePage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val groupRatioPercentage = \"Enter the group ratio percentage\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "s|ROUTING_PLACEHOLDER|elections\/${kebabClassName}|g" ../generated-it/controllers/GroupRatioPercentageControllerISpec.scala

echo "Migration GroupRatioPercentage completed"
