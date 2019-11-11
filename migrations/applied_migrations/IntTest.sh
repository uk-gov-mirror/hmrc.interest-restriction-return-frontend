#!/bin/bash

echo ""
echo "Applying migration IntTest"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /intTest                  controllers.IntTestController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /intTest                  controllers.IntTestController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeIntTest                        controllers.IntTestController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeIntTest                        controllers.IntTestController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "intTest.title = IntTest" >> ../conf/messages.en
echo "intTest.heading = IntTest" >> ../conf/messages.en
echo "intTest.checkYourAnswersLabel = IntTest" >> ../conf/messages.en
echo "intTest.error.nonNumeric = Enter your intTest using numbers" >> ../conf/messages.en
echo "intTest.error.required = Enter your intTest" >> ../conf/messages.en
echo "intTest.error.wholeNumber = Enter your intTest using whole numbers" >> ../conf/messages.en
echo "intTest.error.outOfRange = IntTest must be between {0} and {1}" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryIntTestUserAnswersEntry: Arbitrary[(IntTestPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[IntTestPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryIntTestPage: Arbitrary[IntTestPage.type] =";\
    print "    Arbitrary(IntTestPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(IntTestPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def intTest: Option[AnswerRow] = userAnswers.get(IntTestPage) map {";\
     print "    x =>";\
     print "      AnswerRow(";\
     print "        HtmlFormat.escape(messages(\"intTest.checkYourAnswersLabel\")),";\
     print "        HtmlFormat.escape(x.toString),";\
     print "        routes.IntTestController.onPageLoad(CheckMode).url";\
     print "      )"
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration IntTest completed"
