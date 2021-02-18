#!/bin/bash

echo ""
echo "Applying migration EstimatedFigures"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/groupLevelInformation.routes
echo "### EstimatedFigures Controller" >> ../conf/groupLevelInformation.routes
echo "### ----------------------------------------" >> ../conf/groupLevelInformation.routes
export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "EstimatedFigures" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.groupLevelInformation.EstimatedFiguresController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/groupLevelInformation.routes
echo "POST       /$kebabClassName                          controllers.groupLevelInformation.EstimatedFiguresController.onSubmit(mode: Mode = NormalMode)" >> ../conf/groupLevelInformation.routes
echo "GET        /$kebabClassName/change                   controllers.groupLevelInformation.EstimatedFiguresController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/groupLevelInformation.routes
echo "POST       /$kebabClassName/change                   controllers.groupLevelInformation.EstimatedFiguresController.onSubmit(mode: Mode = CheckMode)" >> ../conf/groupLevelInformation.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# EstimatedFiguresPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "estimatedFigures.title = Tell us which figures have been estimated" >> ../conf/messages.en
echo "estimatedFigures.heading = Tell us which figures have been estimated" >> ../conf/messages.en
echo "estimatedFigures.aNGIE = ANGIE" >> ../conf/messages.en
echo "estimatedFigures.qNGIE = QNGIE" >> ../conf/messages.en
echo "estimatedFigures.checkYourAnswersLabel = Tell us which figures have been estimated" >> ../conf/messages.en
echo "estimatedFigures.error.required = Select estimatedFigures" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# EstimatedFiguresPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "estimatedFigures.title = Tell us which figures have been estimated" >> ../conf/messages.cy
echo "estimatedFigures.heading = Tell us which figures have been estimated" >> ../conf/messages.cy
echo "estimatedFigures.aNGIE = ANGIE" >> ../conf/messages.cy
echo "estimatedFigures.qNGIE = QNGIE" >> ../conf/messages.cy
echo "estimatedFigures.checkYourAnswersLabel = Tell us which figures have been estimated" >> ../conf/messages.cy
echo "estimatedFigures.error.required = Select estimatedFigures" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryEstimatedFiguresUserAnswersEntry: Arbitrary[(EstimatedFiguresPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[EstimatedFiguresPage.type]";\
    print "        value <- arbitrary[EstimatedFigures].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryEstimatedFiguresPage: Arbitrary[EstimatedFiguresPage.type] =";\
    print "    Arbitrary(EstimatedFiguresPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryEstimatedFigures: Arbitrary[EstimatedFigures] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(EstimatedFigures.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(EstimatedFiguresPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def estimatedFigures: Option[SummaryListRow] = answer(EstimatedFiguresPage, groupLevelInformationRoutes.EstimatedFiguresController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    EstimatedFiguresPage.toString -> EstimatedFiguresPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    EstimatedFiguresPage.toString -> EstimatedFiguresPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val estimatedFigures = \"Tell us which figures have been estimated\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|groupLevelInformation\/${kebabClassName}|g" ../generated-it/controllers/groupLevelInformation/EstimatedFiguresControllerISpec.scala

echo "Migration EstimatedFigures completed"
