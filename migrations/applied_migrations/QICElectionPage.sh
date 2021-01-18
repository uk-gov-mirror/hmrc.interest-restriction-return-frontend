#!/bin/bash

echo ""
echo "Applying migration QICElectionPage"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/elections.routes
echo "### QICElectionPage Controller" >> ../conf/elections.routes
echo "### ----------------------------------------" >> ../conf/elections.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "QICElectionPage" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.elections.QICElectionPageController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/elections.routes
echo "POST       /$kebabClassName                          controllers.elections.QICElectionPageController.onSubmit(mode: Mode = NormalMode)" >> ../conf/elections.routes
echo "GET        /$kebabClassName/change                   controllers.elections.QICElectionPageController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/elections.routes
echo "POST       /$kebabClassName/change                   controllers.elections.QICElectionPageController.onSubmit(mode: Mode = CheckMode)" >> ../conf/elections.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# QICElectionPagePage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "qICElectionPage.title = Has a company made a qualifying infrastructure company (QIC) election for the return period?" >> ../conf/messages.en
echo "qICElectionPage.heading = Has a company made a qualifying infrastructure company (QIC) election for the return period?" >> ../conf/messages.en
echo "qICElectionPage.checkYourAnswersLabel = Has a company made a qualifying infrastructure company (QIC) election for the return period?" >> ../conf/messages.en
echo "qICElectionPage.error.required = Select yes if Has a company made a qualifying infrastructure company (QIC) election for the return period?" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryQICElectionPageUserAnswersEntry: Arbitrary[(QICElectionPagePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[QICElectionPagePage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryQICElectionPagePage: Arbitrary[QICElectionPagePage.type] =";\
    print "    Arbitrary(QICElectionPagePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(QICElectionPagePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def qICElectionPage: Option[SummaryListRow] = answer(QICElectionPagePage, electionsRoutes.QICElectionPageController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    QICElectionPagePage.toString -> QICElectionPagePage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    QICElectionPagePage.toString -> QICElectionPagePage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val qICElectionPage = \"Has a company made a qualifying infrastructure company (QIC) election for the return period?\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|elections\/${kebabClassName}|g" ../generated-it/controllers/elections/QICElectionPageControllerISpec.scala

echo "Migration QICElectionPage completed"
