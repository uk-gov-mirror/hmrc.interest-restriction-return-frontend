#!/bin/bash

echo ""
echo "Applying migration AgentDetails"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /agentDetails                        controllers.AgentDetailsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /agentDetails                        controllers.AgentDetailsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAgentDetails                  controllers.AgentDetailsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAgentDetails                  controllers.AgentDetailsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# AgentDetailsPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "agentDetails.title = agentDetails" >> ../conf/messages.en
echo "agentDetails.heading = agentDetails" >> ../conf/messages.en
echo "agentDetails.checkYourAnswersLabel = agentDetails" >> ../conf/messages.en
echo "agentDetails.error.required = Select yes if agentDetails" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# AgentDetailsPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "agentDetails.title = agentDetails" >> ../conf/messages.cy
echo "agentDetails.heading = agentDetails" >> ../conf/messages.cy
echo "agentDetails.checkYourAnswersLabel = agentDetails" >> ../conf/messages.cy
echo "agentDetails.error.required = Select yes if agentDetails" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAgentDetailsUserAnswersEntry: Arbitrary[(AgentDetailsPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AgentDetailsPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAgentDetailsPage: Arbitrary[AgentDetailsPage.type] =";\
    print "    Arbitrary(AgentDetailsPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AgentDetailsPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def agentDetails: Option[SummaryListRow] = answer(AgentDetailsPage, routes.AgentDetailsController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding template to Nunjucks templates"
echo "object AgentDetailsTemplate extends WithName(\"agentDetails.njk\") with ViewTemplate" >> ../app/nunjucks/ViewTemplate.scala

echo "Migration AgentDetails completed"
