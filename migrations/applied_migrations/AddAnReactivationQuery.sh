#!/bin/bash

echo ""
echo "Applying migration AddAnReactivationQuery"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/ukCompanies.routes
echo "### AddAnReactivationQuery Controller" >> ../conf/ukCompanies.routes
echo "### ----------------------------------------" >> ../conf/ukCompanies.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "AddAnReactivationQuery" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.ukCompanies.AddAnReactivationQueryController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/ukCompanies.routes
echo "POST       /$kebabClassName                          controllers.ukCompanies.AddAnReactivationQueryController.onSubmit(mode: Mode = NormalMode)" >> ../conf/ukCompanies.routes
echo "GET        /$kebabClassName/change                   controllers.ukCompanies.AddAnReactivationQueryController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/ukCompanies.routes
echo "POST       /$kebabClassName/change                   controllers.ukCompanies.AddAnReactivationQueryController.onSubmit(mode: Mode = CheckMode)" >> ../conf/ukCompanies.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# AddAnReactivationQueryPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "addAnReactivationQuery.title = Do you need to add a reactivation to this company?" >> ../conf/messages.en
echo "addAnReactivationQuery.heading = Do you need to add a reactivation to this company?" >> ../conf/messages.en
echo "addAnReactivationQuery.checkYourAnswersLabel = Do you need to add a reactivation to this company?" >> ../conf/messages.en
echo "addAnReactivationQuery.error.required = Select yes if Do you need to add a reactivation to this company?" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# AddAnReactivationQueryPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "addAnReactivationQuery.title = Do you need to add a reactivation to this company?" >> ../conf/messages.cy
echo "addAnReactivationQuery.heading = Do you need to add a reactivation to this company?" >> ../conf/messages.cy
echo "addAnReactivationQuery.checkYourAnswersLabel = Do you need to add a reactivation to this company?" >> ../conf/messages.cy
echo "addAnReactivationQuery.error.required = Select yes if Do you need to add a reactivation to this company?" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAddAnReactivationQueryUserAnswersEntry: Arbitrary[(AddAnReactivationQueryPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AddAnReactivationQueryPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAddAnReactivationQueryPage: Arbitrary[AddAnReactivationQueryPage.type] =";\
    print "    Arbitrary(AddAnReactivationQueryPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AddAnReactivationQueryPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def addAnReactivationQuery: Option[SummaryListRow] = answer(AddAnReactivationQueryPage, ukCompaniesRoutes.AddAnReactivationQueryController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    AddAnReactivationQueryPage.toString -> AddAnReactivationQueryPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    AddAnReactivationQueryPage.toString -> AddAnReactivationQueryPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val addAnReactivationQuery = \"Do you need to add a reactivation to this company?\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|ukCompanies\/${kebabClassName}|g" ../generated-it/controllers/ukCompanies/AddAnReactivationQueryControllerISpec.scala

echo "Migration AddAnReactivationQuery completed"
