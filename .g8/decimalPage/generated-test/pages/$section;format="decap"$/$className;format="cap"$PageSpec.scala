package pages.$section;format="decap"$

import pages.behaviours.PageBehaviours

class $className;format="cap"$PageSpec extends PageBehaviours {

  "$className;format="cap"$Page" must {

    beRetrievable[BigDecimal]($className;format="cap"$Page)

    beSettable[BigDecimal]($className;format="cap"$Page)

    beRemovable[BigDecimal]($className;format="cap"$Page)
  }
}
