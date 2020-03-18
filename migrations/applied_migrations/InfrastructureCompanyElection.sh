#!/bin/bash

echo ""
echo "Applying migration InfrastructureCompanyElection"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /infrastructureCompanyElection                        controllers.groupLevelInformation.InfrastructureCompanyElectionController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /infrastructureCompanyElection                        controllers.groupLevelInformation.InfrastructureCompanyElectionController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeInfrastructureCompanyElection                  controllers.groupLevelInformation.InfrastructureCompanyElectionController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeInfrastructureCompanyElection                  controllers.groupLevelInformation.InfrastructureCompanyElectionController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# InfrastructureCompanyElectionPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "infrastructureCompanyElection.title = Has the group made the Infrastructure company election?" >> ../conf/messages.en
echo "infrastructureCompanyElection.heading = Has the group made the Infrastructure company election?" >> ../conf/messages.en
echo "infrastructureCompanyElection.checkYourAnswersLabel = Has the group made the Infrastructure company election?" >> ../conf/messages.en
echo "infrastructureCompanyElection.error.required = Select yes if Has the group made the Infrastructure company election?" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# InfrastructureCompanyElectionPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "infrastructureCompanyElection.title = Has the group made the Infrastructure company election?" >> ../conf/messages.cy
echo "infrastructureCompanyElection.heading = Has the group made the Infrastructure company election?" >> ../conf/messages.cy
echo "infrastructureCompanyElection.checkYourAnswersLabel = Has the group made the Infrastructure company election?" >> ../conf/messages.cy
echo "infrastructureCompanyElection.error.required = Select yes if Has the group made the Infrastructure company election?" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryInfrastructureCompanyElectionUserAnswersEntry: Arbitrary[(InfrastructureCompanyElectionPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[InfrastructureCompanyElectionPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryInfrastructureCompanyElectionPage: Arbitrary[InfrastructureCompanyElectionPage.type] =";\
    print "    Arbitrary(InfrastructureCompanyElectionPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(InfrastructureCompanyElectionPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def infrastructureCompanyElection: Option[SummaryListRow] = answer(InfrastructureCompanyElectionPage, routes.InfrastructureCompanyElectionController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration InfrastructureCompanyElection completed"
