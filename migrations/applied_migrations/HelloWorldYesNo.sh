#!/bin/bash

echo ""
echo "Applying migration HelloWorldYesNo"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /helloWorldYesNo                        controllers.HelloWorldYesNoController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /helloWorldYesNo                        controllers.HelloWorldYesNoController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeHelloWorldYesNo                  controllers.HelloWorldYesNoController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeHelloWorldYesNo                  controllers.HelloWorldYesNoController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "helloWorldYesNo.title = helloWorldYesNo" >> ../conf/messages.en
echo "helloWorldYesNo.heading = helloWorldYesNo" >> ../conf/messages.en
echo "helloWorldYesNo.checkYourAnswersLabel = helloWorldYesNo" >> ../conf/messages.en
echo "helloWorldYesNo.error.required = Select yes if helloWorldYesNo" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryHelloWorldYesNoUserAnswersEntry: Arbitrary[(HelloWorldYesNoPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[HelloWorldYesNoPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryHelloWorldYesNoPage: Arbitrary[HelloWorldYesNoPage.type] =";\
    print "    Arbitrary(HelloWorldYesNoPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(HelloWorldYesNoPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def helloWorldYesNo: Option[AnswerRow] = userAnswers.get(HelloWorldYesNoPage) map {";\
     print "    x =>";\
     print "      AnswerRow(";\
     print "        HtmlFormat.escape(messages(\"helloWorldYesNo.checkYourAnswersLabel\")),";\
     print "        yesOrNo(x),";\
     print "        routes.HelloWorldYesNoController.onPageLoad(CheckMode).url";\
     print "      )"
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration HelloWorldYesNo completed"
