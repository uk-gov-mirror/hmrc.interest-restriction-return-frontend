#!/bin/bash

echo ""
echo "Applying migration CompanyDetails"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/ukCompanies.routes
echo "### CompanyDetails Controller" >> ../conf/ukCompanies.routes
echo "### ----------------------------------------" >> ../conf/ukCompanies.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "CompanyDetails" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.ukCompanies.CompanyDetailsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/ukCompanies.routes
echo "POST       /$kebabClassName                          controllers.ukCompanies.CompanyDetailsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/ukCompanies.routes
echo "GET        /$kebabClassName/change                   controllers.ukCompanies.CompanyDetailsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/ukCompanies.routes
echo "POST       /$kebabClassName/change                   controllers.ukCompanies.CompanyDetailsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/ukCompanies.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# CompanyDetailsPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "companyDetails.title = companyDetails" >> ../conf/messages.en
echo "companyDetails.heading = companyDetails" >> ../conf/messages.en
echo "companyDetails.checkYourAnswersLabel = companyDetails" >> ../conf/messages.en
echo "companyDetails.label= Company name" >> ../conf/messages.en
echo "companyDetails.error.required = Enter companyDetails" >> ../conf/messages.en
echo "companyDetails.error.length = CompanyDetails must be 160 characters or less" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# CompanyDetailsPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "companyDetails.title = companyDetails" >> ../conf/messages.cy
echo "companyDetails.heading = companyDetails" >> ../conf/messages.cy
echo "companyDetails.checkYourAnswersLabel = companyDetails" >> ../conf/messages.cy
echo "companyDetails.label = Company name" >> ../conf/messages.cy
echo "companyDetails.error.required = Enter companyDetails" >> ../conf/messages.cy
echo "companyDetails.error.length = CompanyDetails must be 160 characters or less" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCompanyDetailsUserAnswersEntry: Arbitrary[(CompanyDetailsPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[CompanyDetailsPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCompanyDetailsPage: Arbitrary[CompanyDetailsPage.type] =";\
    print "    Arbitrary(CompanyDetailsPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(CompanyDetailsPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def companyDetails: Option[SummaryListRow] = answer(CompanyDetailsPage, ukCompaniesRoutes.CompanyDetailsController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    CompanyDetailsPage.toString -> CompanyDetailsPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    CompanyDetailsPage.toString -> CompanyDetailsPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val companyDetails = \"Company details\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|ukCompanies\/${kebabClassName}|g" ../generated-it/controllers/ukCompanies/CompanyDetailsControllerISpec.scala

echo "Migration CompanyDetails completed"
