package com.mavis.boot.common.mapstruct;

public class Main {

    public static void main(String[] args) {
        Source s = new Source();
        s.setTest("5");

        Target t = SourceTargetMapper.MAPPER.toTarget(s);
        System.out.println(t.getTesting());
    }
}