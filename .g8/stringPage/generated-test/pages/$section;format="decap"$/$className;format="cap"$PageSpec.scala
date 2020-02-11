package pages.$section;format="decap"$

import pages.behaviours.PageBehaviours

class $className;format="cap"$PageSpec extends PageBehaviours {

  "$className;format="cap"$Page" must {

    beRetrievable[String]($className;format="cap"$Page)

    beSettable[String]($className;format="cap"$Page)

    beRemovable[String]($className;format="cap"$Page)
  }
}
