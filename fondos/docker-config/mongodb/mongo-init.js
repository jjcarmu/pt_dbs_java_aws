db = db.getSiblingDB("btg_test");
db.createCollection("btg_test");

db.createUser(
        {
            user: "btg_test",
            pwd: "password",
            roles: [
                {
                    role: "readWrite",
                    db: "btg_test"
                }
            ]
        }
);