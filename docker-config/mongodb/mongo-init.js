db = db.getSiblingDB("btg_test");
db.createCollection("init");

db.createUser(
        {
            user: "btg_test",
            pwd: "password",
            roles: [
                {
                    role: "readWrite",
                    db: "company"
                }
            ]
        }
);