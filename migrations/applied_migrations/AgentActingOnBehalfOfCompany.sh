#!/bin/bash

echo ""
echo "Applying migration AgentActingOnBehalfOfCompany"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /agentActingOnBehalfOfCompany                        controllers.startReturn.AgentActingOnBehalfOfCompanyController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /agentActingOnBehalfOfCompany                        controllers.startReturn.AgentActingOnBehalfOfCompanyController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAgentActingOnBehalfOfCompany                  controllers.startReturn.AgentActingOnBehalfOfCompanyController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAgentActingOnBehalfOfCompany                  controllers.startReturn.AgentActingOnBehalfOfCompanyController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# AgentActingOnBehalfOfCompanyPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "agentActingOnBehalfOfCompany.title = agentActingOnBehalfOfCompany" >> ../conf/messages.en
echo "agentActingOnBehalfOfCompany.heading = agentActingOnBehalfOfCompany" >> ../conf/messages.en
echo "agentActingOnBehalfOfCompany.checkYourAnswersLabel = agentActingOnBehalfOfCompany" >> ../conf/messages.en
echo "agentActingOnBehalfOfCompany.error.required = Select yes if agentActingOnBehalfOfCompany" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# AgentActingOnBehalfOfCompanyPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "agentActingOnBehalfOfCompany.title = agentActingOnBehalfOfCompany" >> ../conf/messages.cy
echo "agentActingOnBehalfOfCompany.heading = agentActingOnBehalfOfCompany" >> ../conf/messages.cy
echo "agentActingOnBehalfOfCompany.checkYourAnswersLabel = agentActingOnBehalfOfCompany" >> ../conf/messages.cy
echo "agentActingOnBehalfOfCompany.error.required = Select yes if agentActingOnBehalfOfCompany" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAgentActingOnBehalfOfCompanyUserAnswersEntry: Arbitrary[(AgentActingOnBehalfOfCompanyPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AgentActingOnBehalfOfCompanyPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAgentActingOnBehalfOfCompanyPage: Arbitrary[AgentActingOnBehalfOfCompanyPage.type] =";\
    print "    Arbitrary(AgentActingOnBehalfOfCompanyPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AgentActingOnBehalfOfCompanyPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def agentActingOnBehalfOfCompany: Option[SummaryListRow] = answer(AgentActingOnBehalfOfCompanyPage, routes.AgentActingOnBehalfOfCompanyController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration AgentActingOnBehalfOfCompany completed"
