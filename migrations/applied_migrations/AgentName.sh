#!/bin/bash

echo ""
echo "Applying migration AgentName"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /agentName                        controllers.startReturn.AgentNameController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /agentName                        controllers.startReturn.AgentNameController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAgentName                  controllers.startReturn.AgentNameController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAgentName                  controllers.startReturn.AgentNameController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# AgentNamePage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "agentName.title = agentName" >> ../conf/messages.en
echo "agentName.heading = agentName" >> ../conf/messages.en
echo "agentName.checkYourAnswersLabel = agentName" >> ../conf/messages.en
echo "agentName.label = Agent name" >> ../conf/messages.en
echo "agentName.error.required = Enter agentName" >> ../conf/messages.en
echo "agentName.error.length = AgentName must be 160 characters or less" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# AgentNamePage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "agentName.title = agentName" >> ../conf/messages.cy
echo "agentName.heading = agentName" >> ../conf/messages.cy
echo "agentName.checkYourAnswersLabel = agentName" >> ../conf/messages.cy
echo "agentName.label = Agent name" >> ../conf/messages.cy
echo "agentName.error.required = Enter agentName" >> ../conf/messages.cy
echo "agentName.error.length = AgentName must be 160 characters or less" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAgentNameUserAnswersEntry: Arbitrary[(AgentNamePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AgentNamePage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAgentNamePage: Arbitrary[AgentNamePage.type] =";\
    print "    Arbitrary(AgentNamePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AgentNamePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def agentName: Option[SummaryListRow] = answer(AgentNamePage, routes.AgentNameController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding template to Nunjucks templates"
echo "object AgentNameTemplate extends WithName(\"agentName.njk\") with ViewTemplate" >> ../app/nunjucks/ViewTemplate.scala

echo "Migration AgentName completed"
