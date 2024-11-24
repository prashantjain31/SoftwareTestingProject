package org.example.person;

import org.example.files.Files;

public class Member extends Person {
    private static int staticID = 1;

    public Member(String name, long nationalCode) {
        super(name, nationalCode);
        this.ID = staticID;
        staticID++;
    }

    public static void setStaticID(int staticID) {
        Member.staticID = staticID;
    }

    public static int getStaticID() {
        return staticID;
    }

    @Override
    public String save() {
        return Files.save(this);
    }

    @Override
    public String remove() {
        if (this.ID == staticID - 1)
            staticID--;

        return Files.remove(this);
    }
}
