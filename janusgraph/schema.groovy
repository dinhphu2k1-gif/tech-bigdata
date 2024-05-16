import org.apache.tinkerpop.gremlin.structure.Edge
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.janusgraph.core.Cardinality
import org.janusgraph.core.Multiplicity

/**
 * CHANGE LOG
 * đánh composite index cho các trường cần truy vấn (các trường null cho là -1)
 * bỏ unique index
 */

/**
 * Vertex Label
 * */
VERTEX_LABEL_PII = "pii"
VERTEX_LABEL_APP = "app"
VERTEX_LABEL_WEB_MOBILE = "webMb"
VERTEX_LABEL_WEB_PC = "webPc"

/**
 * Edge Label
 */
EDGE_LABEL_MATCHING = "matching"
EDGE_LABEL_EXTENDS = "extends"

/**
 * Properties
 */

// common
PROP_KEY_ID = "id"
PROP_KEY_CREATED_TIME = "createdTime"
PROP_KEY_UPDATED_TIME = "updatedTime"
PROP_KEY_IS_ACTIVE = "isActive"

// là 1 property
PROP_KEY_VERTEX_LABEL = "vertexLabel"
PROP_KEY_EDGE_LABEL = "edgeLabel"

// pii
PROP_KEY_PII = "pii"

// app
PROP_KEY_APP_ID = "appId"
PROP_KEY_DEVICE_ID = "deviceId"
PROP_KEY_INSTALL_ID = "installId"
PROP_KEY_FIRE_BASE_ID = "firebaseId"
PROP_KEY_IFA = "ifa"
PROP_KEY_KEYCHAIN = "keychain"
PROP_KEY_OS_TYPE = "osType"
PROP_KEY_USER_ID = "userId"

// web PC & MB
PROP_KEY_DOMAIN = "domain"
PROP_KEY_DOMAIN_GUID = "domainGuid"
PROP_KEY_MATCH = "matchDomain"
PROP_KEY_FINGERPRINT = "fingerprint"
PROP_KEY_GUID = "guid"
PROP_KEY_GA = "ga"

// edge
PROP_KEY_INFO_EDGE = "infoEdge"

graph = JanusGraphFactory.open(args[0])
mgmt = graph.openManagement()
if (mgmt.getPropertyKey(PROP_KEY_ID) == null) {
    try {
        println("---> Create Schema...")

        /**
         * Properties
         * */

        // common
        ids = mgmt.makePropertyKey(PROP_KEY_ID).dataType(UUID.class).cardinality(Cardinality.SINGLE).make();
        createdTime = mgmt.makePropertyKey(PROP_KEY_CREATED_TIME).dataType(Long.class).cardinality(Cardinality.SINGLE).make();
        updatedTime = mgmt.makePropertyKey(PROP_KEY_UPDATED_TIME).dataType(Long.class).cardinality(Cardinality.SINGLE).make();
        vertexLabel = mgmt.makePropertyKey(PROP_KEY_VERTEX_LABEL).dataType(String.class).cardinality(Cardinality.SINGLE).make();

        // edge
        infoEdge = mgmt.makePropertyKey(PROP_KEY_INFO_EDGE).dataType(String.class).cardinality(Cardinality.SINGLE).make();
        edgeLabel= mgmt.makePropertyKey(PROP_KEY_EDGE_LABEL).dataType(String.class).cardinality(Cardinality.SINGLE).make();

        // pii
        piiId = mgmt.makePropertyKey(PROP_KEY_PII).dataType(String.class).cardinality(Cardinality.SINGLE).make();

        // chung cho info vertex
        isActive = mgmt.makePropertyKey(PROP_KEY_IS_ACTIVE).dataType(Boolean.class).cardinality(Cardinality.SINGLE).make();

        // app
        appId = mgmt.makePropertyKey(PROP_KEY_APP_ID).dataType(String.class).cardinality(Cardinality.SINGLE).make();
        deviceId = mgmt.makePropertyKey(PROP_KEY_DEVICE_ID).dataType(String.class).cardinality(Cardinality.SINGLE).make();
        installId = mgmt.makePropertyKey(PROP_KEY_INSTALL_ID).dataType(String.class).cardinality(Cardinality.SINGLE).make();
        firebaseId = mgmt.makePropertyKey(PROP_KEY_FIRE_BASE_ID).dataType(String.class).cardinality(Cardinality.SINGLE).make();
        ifa = mgmt.makePropertyKey(PROP_KEY_IFA).dataType(String.class).cardinality(Cardinality.SINGLE).make();
        keychain = mgmt.makePropertyKey(PROP_KEY_KEYCHAIN).dataType(String.class).cardinality(Cardinality.SINGLE).make();
        osType = mgmt.makePropertyKey(PROP_KEY_OS_TYPE).dataType(Integer.class).cardinality(Cardinality.SINGLE).make();
        userId = mgmt.makePropertyKey(PROP_KEY_USER_ID).dataType(String.class).cardinality(Cardinality.SINGLE).make();

        // web PC $ MB
        domain = mgmt.makePropertyKey(PROP_KEY_DOMAIN).dataType(String.class).cardinality(Cardinality.SINGLE).make();
        domainGuid = mgmt.makePropertyKey(PROP_KEY_DOMAIN_GUID).dataType(String.class).cardinality(Cardinality.SINGLE).make();
        matchDomain = mgmt.makePropertyKey(PROP_KEY_MATCH).dataType(String.class).cardinality(Cardinality.SINGLE).make();
        fingerprint = mgmt.makePropertyKey(PROP_KEY_FINGERPRINT).dataType(String.class).cardinality(Cardinality.SINGLE).make();
        guid = mgmt.makePropertyKey(PROP_KEY_GUID).dataType(String.class).cardinality(Cardinality.SINGLE).make();
        ga = mgmt.makePropertyKey(PROP_KEY_GA).dataType(String.class).cardinality(Cardinality.SINGLE).make();

        /**
         * Vertex Label
         * */
        pii = mgmt.makeVertexLabel(VERTEX_LABEL_PII).make();
        app = mgmt.makeVertexLabel(VERTEX_LABEL_APP).make();
        webMb = mgmt.makeVertexLabel(VERTEX_LABEL_WEB_MOBILE).make();
        webPc = mgmt.makeVertexLabel(VERTEX_LABEL_WEB_PC).make();

        /**
         * Edge Label
         * */
//        matching = mgmt.makeEdgeLabel(EDGE_LABEL_MATCHING).multiplicity(Multiplicity.SIMPLE).signature(createdTime, updatedTime,ids, infoEdge, edgeLabel ).make();
//        extend = mgmt.makeEdgeLabel(EDGE_LABEL_EXTENDS).multiplicity(Multiplicity.SIMPLE).signature(createdTime, updatedTime,ids, infoEdge, edgeLabel).make();
        matching = mgmt.makeEdgeLabel(EDGE_LABEL_MATCHING).multiplicity(Multiplicity.SIMPLE).make();
        extend = mgmt.makeEdgeLabel(EDGE_LABEL_EXTENDS).multiplicity(Multiplicity.SIMPLE).make();

        /**
         * Edge Index
         * */
        // chưa thấy ý nghĩa lắm
//        mgmt.buildEdgeIndex(matching, "matchingEdgeIndex", Direction.BOTH, Order.desc, ids);
//        mgmt.buildEdgeIndex(extend, "extendsEdgeIndex", Direction.BOTH, Order.desc, ids);

        /**
         * Vertex Index
         * */
        // unique Index
//        idIndex = mgmt.buildIndex("byConsistentId", Vertex.class).addKey(ids).unique().buildCompositeIndex();  // test thử bỏ cái unique này đi xem có nhanh hơn k
        idIndex = mgmt.buildIndex("idsIndex", Vertex.class).addKey(ids).buildCompositeIndex();   // bỏ unique contrain đi cho đỡ phải check

        // pii index. bản chất = id của pii đó nên là consistent được
        mgmt.buildIndex("piiIndex", Vertex.class).addKey(piiId).buildCompositeIndex();

        // full has search
        mgmt.buildIndex("allWebIndex", Vertex.class).addKey(domain).addKey(domainGuid).addKey(matchDomain).addKey(fingerprint).addKey(guid).addKey(ga).addKey(vertexLabel).buildCompositeIndex();
        mgmt.buildIndex("allAppIndex", Vertex.class).addKey(osType).addKey(appId).addKey(deviceId).addKey(ifa).addKey(userId).addKey(firebaseId).addKey(installId).addKey(keychain).addKey(vertexLabel).buildCompositeIndex();

        // label index
//        mgmt.buildIndex("vertexLabel", Vertex.class).addKey(vertexLabel).buildCompositeIndex();
//        mgmt.buildIndex("activeLabel", Vertex.class).addKey(isActive).buildCompositeIndex();  // co  mot cho dung active thi phai

        // properties -> get related vertex
//        mgmt.buildIndex("domainIndex", Vertex.class).addKey(domain).buildCompositeIndex();
        mgmt.buildIndex("domainGuidIndex", Vertex.class).addKey(domainGuid).buildCompositeIndex();
        mgmt.buildIndex("matchIndex", Vertex.class).addKey(matchDomain).buildCompositeIndex();
        mgmt.buildIndex("fingerprintIndex", Vertex.class).addKey(fingerprint).buildCompositeIndex();
        mgmt.buildIndex("guidIndex", Vertex.class).addKey(guid).buildCompositeIndex();
        mgmt.buildIndex("gaIndex", Vertex.class).addKey(ga).buildCompositeIndex();

        // app index
//        mgmt.buildIndex("appIdIndex", Vertex.class).addKey(appId).buildCompositeIndex();
        mgmt.buildIndex("deviceIdIndex", Vertex.class).addKey(deviceId).buildCompositeIndex();
        mgmt.buildIndex("ifaIndex", Vertex.class).addKey(ifa).buildCompositeIndex();
        mgmt.buildIndex("userIdIndex", Vertex.class).addKey(userId).buildCompositeIndex();
        mgmt.buildIndex("firebaseIdIndex", Vertex.class).addKey(firebaseId).buildCompositeIndex();
        mgmt.buildIndex("installIdIndex", Vertex.class).addKey(installId).buildCompositeIndex();
        mgmt.buildIndex("keychainIndex", Vertex.class).addKey(keychain).buildCompositeIndex();
//        mgmt.buildIndex("osTypeIndex", Vertex.class).addKey(osType).buildCompositeIndex();

        /**
         * Mixed Index -> has elastic search
         * */

        mgmt.buildIndex("searchWebPc", Vertex.class)
                .addKey(ids)
                .addKey(domain)
                .addKey(domainGuid)
                .addKey(matchDomain)
                .addKey(fingerprint)
                .addKey(guid)
                .addKey(vertexLabel)
                .addKey(createdTime)
                .addKey(updatedTime)
                .addKey(ga)
                .addKey(isActive)
                .indexOnly(webPc)
                .buildMixedIndex("search");

        mgmt.buildIndex("searchWebMb", Vertex.class)
                .addKey(ids)
                .addKey(domain)
                .addKey(domainGuid)
                .addKey(matchDomain)
                .addKey(fingerprint)
                .addKey(guid)
                .addKey(vertexLabel)
                .addKey(createdTime)
                .addKey(updatedTime)
                .addKey(ga)
                .addKey(isActive)
                .indexOnly(webMb)
                .buildMixedIndex("search");

        mgmt.buildIndex("searchPii", Vertex.class)
                .addKey(ids)
                .addKey(vertexLabel)
                .addKey(createdTime)
                .addKey(updatedTime)
                .indexOnly(pii)
                .buildMixedIndex("search");

        mgmt.buildIndex("searchEdge", Edge.class)
                .addKey(createdTime)
                .addKey(updatedTime)
                .addKey(edgeLabel)
                .addKey(infoEdge)
                .addKey(ids)
                .buildMixedIndex("search");

        mgmt.buildIndex("searchApp", Vertex.class)
                .addKey(ids)

                .addKey(appId)
                .addKey(deviceId)
                .addKey(ifa)
                .addKey(userId)
                .addKey(firebaseId)
                .addKey(installId)
                .addKey(keychain)

                .addKey(osType)

                .addKey(vertexLabel)
                .addKey(isActive)
                .addKey(createdTime)
                .addKey(updatedTime)
                .indexOnly(app)
                .buildMixedIndex("search");

        println("===> Create schema done!!!")
        mgmt.commit()
    } catch (Exception e) {
        e.printStackTrace()
    }
}

mgmt = graph.openManagement()
println(mgmt.printSchema())
