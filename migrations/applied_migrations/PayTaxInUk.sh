#!/bin/bash

echo ""
echo "Applying migration PayTaxInUk"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /payTaxInUk                        controllers.ultimateParentCompany.PayTaxInUkController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /payTaxInUk                        controllers.ultimateParentCompany.PayTaxInUkController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changePayTaxInUk                  controllers.ultimateParentCompany.PayTaxInUkController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changePayTaxInUk                  controllers.ultimateParentCompany.PayTaxInUkController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# PayTaxInUkPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "payTaxInUk.title = Does Name have a Unique Taxpayer Reference?" >> ../conf/messages.en
echo "payTaxInUk.heading = Does Name have a Unique Taxpayer Reference?" >> ../conf/messages.en
echo "payTaxInUk.checkYourAnswersLabel = Does Name have a Unique Taxpayer Reference?" >> ../conf/messages.en
echo "payTaxInUk.error.required = Select yes if Does Name have a Unique Taxpayer Reference?" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# PayTaxInUkPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "payTaxInUk.title = Does Name have a Unique Taxpayer Reference?" >> ../conf/messages.cy
echo "payTaxInUk.heading = Does Name have a Unique Taxpayer Reference?" >> ../conf/messages.cy
echo "payTaxInUk.checkYourAnswersLabel = Does Name have a Unique Taxpayer Reference?" >> ../conf/messages.cy
echo "payTaxInUk.error.required = Select yes if Does Name have a Unique Taxpayer Reference?" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPayTaxInUkUserAnswersEntry: Arbitrary[(PayTaxInUkPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[PayTaxInUkPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPayTaxInUkPage: Arbitrary[PayTaxInUkPage.type] =";\
    print "    Arbitrary(PayTaxInUkPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(PayTaxInUkPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def payTaxInUk: Option[SummaryListRow] = answer(PayTaxInUkPage, routes.PayTaxInUkController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    PayTaxInUkPage.toString -> PayTaxInUkPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "Migration PayTaxInUk completed"
