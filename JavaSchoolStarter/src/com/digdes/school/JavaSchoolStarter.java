package com.digdes.school;

import java.util.*;

public class JavaSchoolStarter {


    List<Map<String, Object>> data = new ArrayList<>();


    public JavaSchoolStarter() {
    }


    private String findValue(List<String> reqArr, String needFindValueOf) {

        String value_ = "";
        int valueI = 0;
        String value = "";
        for (int i = 0; i < reqArr.size(); i++) {
            if (reqArr.get(i).matches("(?i).*" + needFindValueOf + ".*")) {

                value_ = reqArr.get(i);
                valueI = i;
            }
        }
        if (value_.equals("")) {
            value = null;
        }
        value_ = value_.replaceAll("((?i)" + needFindValueOf + ")", "");
        value_ = value_.replace("'", "");
        if (value_.contains("=")) {
            value_ = value_.replace("=", "");
        }
        if (value_.contains(",")) {
            value_ = value_.replace(",", "");
        }
        if (value_.length() != 0 && value != null) {
            value = value_;
        } else if (!reqArr.get(valueI + 1).equals("=") && value != null) {
            value = (reqArr.get(valueI + 1)).replace(",", "").replace("=", "");
        } else if (!reqArr.get(valueI + 2).equals(",") && value != null) {
            value = (reqArr.get(valueI + 2)).replace(",", "").replace("=", "");
        }

        return value;
    }


    //INSERT
    private void insert(List<Map<String, Object>> data, List<String> reqArr) throws Exception {
        Map<String, Object> row = new HashMap<>();
        reqArr.removeIf(String::isEmpty);
        if (reqArr.get(1).matches("(?i).*values.*")) {
            try {
                Long id = (findValue(reqArr, "id") == null) ?
                        null : Long.parseLong(findValue(reqArr, "id"));
                String lastName = (findValue(reqArr, "lastname") == null) ?
                        null : findValue(reqArr, "lastname");
                if (lastName != null && lastName.matches("\\d+")) throw new Exception("!!!Exception!!! lastName не " +
                        "может быть числом");
                Double cost = (findValue(reqArr, "cost") == null) ?
                        null : Double.parseDouble(findValue(reqArr, "'cost'"));
                Long age = (findValue(reqArr, "age") == null) ?
                        null : Long.parseLong(findValue(reqArr, "'age'"));
                Boolean active = (findValue(reqArr, "active") == null) ?
                        null : Boolean.parseBoolean(findValue(reqArr, "active"));


                row.put("id", id);
                row.put("lastName", lastName);
                row.put("age", age);
                row.put("cost", cost);
                row.put("active", active);
                data.add(row);
            } catch (Exception e) {
                throw new Exception("!!!Exception!!!" + e);
            }


        }
    }

    private boolean isContains(List<String> reqArr, String s) {
        boolean contains = false;
        for (String i : reqArr) {
            if (i.matches("(?).*" + s + ".*")) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    //UPDATE
    private void update(List<String> reqArr) throws Exception {
        if (reqArr.toString().matches("(?i).*values.*")) {
            reqArr.remove(reqArr.get(0));
            reqArr.remove(reqArr.get(0));
            reqArr.removeIf(i -> i.equals(","));


            Long newId = null;
            String newLastName = null;
            Long newAge = null;
            Double newCost = null;
            Boolean newActive = null;

            if (reqArr.toString().matches("(?i).*where.*")) {

                int whereI = -1;

                for (String i : reqArr) {
                    if (i.matches("(?i).*where.*")) whereI = reqArr.indexOf(i);
                }

                List<String> beforeWhere = new ArrayList<>();
                List<String> conditionsAfterWhere = new ArrayList<>();

                if (!reqArr.toString().matches("(?i).*and.*") && !reqArr.toString().matches("(?i).*or.*")) {
                    int sizeReqArr = reqArr.size();
                    reqArr.add("or");
                    for (int i = whereI + 1; i < sizeReqArr; i++) {
                        reqArr.add(reqArr.get(i));
                    }
                }

                for (int i = 0; i < whereI; i++) beforeWhere.add(reqArr.get(i));
                for (int i = whereI; i < reqArr.size(); i++) conditionsAfterWhere.add(reqArr.get(i));

                int[] or_and_index = {};
                List<String> and_or = new ArrayList<>();

                for (String i : conditionsAfterWhere) {
                    if (i.matches("(?i)or") || i.matches("(?i)and")) {
                        or_and_index = Arrays.copyOf(or_and_index, or_and_index.length + 1);
                        or_and_index[or_and_index.length - 1] = conditionsAfterWhere.indexOf(i);
                        and_or.add(conditionsAfterWhere.get(conditionsAfterWhere.indexOf(i)));
                    }
                }
                StringBuilder firsCondition = new StringBuilder();
                for (int i = 1; i < or_and_index[0]; i++) {
                    firsCondition.append(conditionsAfterWhere.get(i));
                }

                List<String> conditionsAfterWhere_ = new ArrayList<>(conditionsAfterWhere);
                conditionsAfterWhere.clear();
                conditionsAfterWhere.add("where");
                conditionsAfterWhere.add(firsCondition.toString());

                for (int i = 0; i < or_and_index.length; i++) {
                    conditionsAfterWhere.add(and_or.get(i));
                    StringBuilder condition = new StringBuilder();
                    if (or_and_index[or_and_index.length - 1] == or_and_index[i]) {
                        for (int j = or_and_index[i] + 1; j < conditionsAfterWhere_.size(); j++) {
                            condition.append(conditionsAfterWhere_.get(j));
                        }
                        conditionsAfterWhere.add(condition.toString());
                    } else {
                        for (int k = or_and_index[i] + 1; k < or_and_index[i + 1]; k++) {
                            condition.append(conditionsAfterWhere_.get(k));
                        }
                        conditionsAfterWhere.add(condition.toString());
                    }
                }

                for (String i : beforeWhere) {
                    if (i.matches("(?i).*id.*")) {
                        newId = Long.parseLong(findValue(beforeWhere, "id"));
                    }
                    if (i.matches("(?i).*lastName.*")) {
                        newLastName = findValue(beforeWhere, "lastname");
                    }
                    if (i.matches("(?i).*age.*")) {
                        newAge = Long.parseLong(findValue(beforeWhere, "age"));
                    }
                    if (i.matches("(?i).*cost.*")) {
                        newCost = Double.parseDouble(findValue(beforeWhere, "cost"));
                    }
                    if (i.matches("(?i).*active.*")) {
                        newActive = Boolean.parseBoolean(findValue(beforeWhere, "active"));
                    }
                }
                int countAndOr = 0;
                for (String i : conditionsAfterWhere) {
                    if (i.matches("(?i)or") || i.matches("(?i)and")) {
                        countAndOr++;
                    }
                }


                List<String> selectedReq = new ArrayList<>();
                selectedReq.add("select");
                selectedReq.addAll(conditionsAfterWhere);

                List<Map<String, Object>> selectedData = select(data, selectedReq);


                if (countAndOr == 0) {
                    conditionsAfterWhere.remove((conditionsAfterWhere.size() - 1));
                    conditionsAfterWhere.remove((conditionsAfterWhere.size() - 1));
                }
                for (int i = 0; i < 5; i++) beforeWhere.add("");
                for (Map<String, Object> i : selectedData) {
                    for (Map<String, Object> j : data) {
                        if (j == i) {
                            if (newId != null) {
                                j.put("id", newId);
                            } else if (findValue(beforeWhere, "id") == null && isContains(beforeWhere, "id")) {
                                j.put("id", null);
                            }
                            if (newLastName != null) {
                                j.put("lastName", newLastName);
                            } else if (findValue(beforeWhere, "lastname") == null && isContains(beforeWhere, "lastname")) {
                                j.put("lastName", null);
                            }
                            if (newAge != null) {
                                j.put("age", newAge);
                            } else if (findValue(beforeWhere, "age") == null && isContains(beforeWhere, "age")) {
                                j.put("age", null);
                            }
                            if (newCost != null) {
                                j.put("cost", newCost);
                            } else if (findValue(beforeWhere, "cost") == null && isContains(beforeWhere, "cost")) {
                                j.put("cost", null);
                            }
                            if (newActive != null) {
                                j.put("active", newActive);
                            } else if (findValue(beforeWhere, "active") == null && isContains(beforeWhere, "active")) {
                                j.put("active", null);
                            }
                        }
                    }
                }

            } else {
                for (String i : reqArr) {
                    if (i.matches("(?i).*id.*")) {
                        newId = Long.parseLong(findValue(reqArr, "id"));
                    }
                    if (i.matches("(?i).*lastName.*")) {
                        newLastName = findValue(reqArr, "lastname");
                    }
                    if (i.matches("(?i).*age.*")) {
                        newAge = Long.parseLong(findValue(reqArr, "age"));
                    }
                    if (i.matches("(?i).*cost.*")) {
                        newCost = Double.parseDouble(findValue(reqArr, "cost"));
                    }
                    if (i.matches("(?i).*active.*")) {
                        newActive = Boolean.parseBoolean(findValue(reqArr, "active"));
                    }
                }
                for (Map<String, Object> j : data) {

                    if (newId != null) {
                        j.put("id", newId);
                    } else if (findValue(reqArr, "id") == null && isContains(reqArr, "id")) {
                        j.put("id", null);
                    }
                    if (newLastName != null) {
                        j.put("lastName", newLastName);
                    } else if (findValue(reqArr, "lastname") == null && isContains(reqArr, "lastname")) {
                        j.put("lastName", null);
                    }
                    if (newAge != null) {
                        j.put("age", newAge);
                    } else if (findValue(reqArr, "age") == null && isContains(reqArr, "age")) {
                        j.put("age", null);
                    }
                    if (newCost != null) {
                        j.put("cost", newCost);
                    } else if (findValue(reqArr, "cost") == null && isContains(reqArr, "cost")) {
                        j.put("cost", null);
                    }
                    if (newActive != null) {
                        j.put("active", newActive);
                    } else if (findValue(reqArr, "active") == null && isContains(reqArr, "active")) {
                        j.put("active", null);
                    }

                }
            }

        }
    }


    //DELETE
    private void delete(List<String> reqArr) throws Exception {
        if (reqArr.size() == 1) {
            data.clear();
        } else if (reqArr.toString().matches("(?i).*where.*")) {
            List<String> reqArrCopy = new ArrayList<>(reqArr);
            reqArr.clear();
            reqArr.add("select");
            for (int i = 1; i < reqArrCopy.size(); i++) {
                reqArr.add(reqArrCopy.get(i));
            }
            List<Map<String, Object>> WhatToDelete = new ArrayList<>(select(data, reqArr));
            data.removeAll(WhatToDelete);
        }
    }


    private List<Map<String, Object>> conditionAnd(List<Map<String, Object>> someData, String condition) throws Exception {
        //id
        if (condition.matches("(?i).*id.*")) {
            condition = condition.replaceAll("(?i)'id'", "");
            if (condition.matches(".*>.*") && !condition.matches(".*=.*")) {
                condition = condition.replaceAll("(?i)>", "");
                try {
                    long idCondition = Long.parseLong(condition);
                    List<Map<String, Object>> someData_ = new ArrayList<>();
                    for (Map<String, Object> i : someData) {
                        if (i.get("id") != null) {
                            if (Long.parseLong(i.get("id").toString()) > idCondition) {
                                someData_.add(i);
                            }
                        }
                        someData = someData_;
                    }
                } catch (Exception e) {
                    throw new Exception("!!!Exception!!! " + e);
                }

            } else if (condition.matches(".*<.*") && !condition.matches(".*=.*")) {
                condition = condition.replaceAll("(?i)<", "");
                try {
                    long idCondition = Long.parseLong(condition);
                    List<Map<String, Object>> someData_ = new ArrayList<>();
                    for (Map<String, Object> i : someData) {
                        if (i.get("id") != null) {
                            if (Long.parseLong(i.get("id").toString()) < idCondition) {
                                someData_.add(i);
                            }
                        }
                        someData = someData_;
                    }
                } catch (Exception e) {
                    System.out.println("!!!Exception!!!: " + e);
                }
            } else if (condition.matches(".*=.*") &&
                    !condition.matches(".*>.*") &&
                    !condition.matches(".*<.*") &&
                    !condition.matches(".*!.*")) {
                condition = condition.replaceAll("(?i)=", "");
                try {
                    long idCondition = Long.parseLong(condition);
                    List<Map<String, Object>> someData_ = new ArrayList<>();
                    for (Map<String, Object> i : someData) {
                        if (i.get("id") != null) {
                            if (Long.parseLong(i.get("id").toString()) == idCondition) {
                                someData_.add(i);
                            }
                        }
                        someData = someData_;
                    }
                } catch (Exception e) {
                    throw new Exception("!!!Exception!!! " + e);
                }
            } else if (condition.matches(".*!.*") && condition.matches(".*=.*")) {
                condition = condition.replaceAll("(?i)!=", "");
                try {
                    long idCondition = Long.parseLong(condition);
                    List<Map<String, Object>> someData_ = new ArrayList<>();
                    for (Map<String, Object> i : someData) {
                        if (i.get("id") != null) {
                            if (Long.parseLong(i.get("id").toString()) != idCondition) {
                                someData_.add(i);
                            }
                        }
                        someData = someData_;
                    }
                } catch (Exception e) {
                    throw new Exception("!!!Exception!!! " + e);
                }

            } else if (condition.matches(".*<.*") && condition.matches(".*=.*")) {
                condition = condition.replaceAll("(?i)<=", "");
                try {
                    long idCondition = Long.parseLong(condition);
                    List<Map<String, Object>> someData_ = new ArrayList<>();
                    for (Map<String, Object> i : someData) {
                        if (i.get("id") != null) {
                            if (Long.parseLong(i.get("id").toString()) <= idCondition) {
                                someData_.add(i);
                            }
                        }
                        someData = someData_;
                    }
                } catch (Exception e) {
                    throw new Exception("!!!Exception!!! " + e);
                }

            } else if (condition.matches(".*>.*") && condition.matches(".*=.*")) {
                condition = condition.replaceAll("(?i)>=", "");
                try {
                    long idCondition = Long.parseLong(condition);
                    List<Map<String, Object>> someData_ = new ArrayList<>();
                    for (Map<String, Object> i : someData) {
                        if (i.get("id") != null) {
                            if (Long.parseLong(i.get("id").toString()) >= idCondition) {
                                someData_.add(i);
                            }
                        }
                        someData = someData_;
                    }
                } catch (Exception e) {
                    throw new Exception("!!!Exception!!! " + e);
                }
            }
            //lastName
        } else if (condition.matches("(?i).*lastname.*")) {
            condition = condition.replaceAll("(?i)'lastname'", "");
            if (condition.matches(".*=.*") && !condition.matches(".*!.*")) {
                condition = condition.replaceAll("(?i)=", "");
                String lastNameCondition = condition;
                if (condition.matches("\\d+")) {
                    throw new Exception("!!!Exception!!!");
                }
                List<Map<String, Object>> someData_ = new ArrayList<>();
                for (Map<String, Object> i : someData) {
                    if (i.get("lastName") != null) {
                        if (i.get("lastName").toString().equals(lastNameCondition)) {
                            someData_.add(i);
                        }
                    }
                    someData = someData_;
                }
            } else if (condition.matches(".*!.*") && condition.matches(".*=.*")) {
                condition = condition.replaceAll("(?i)!=", "");
                String lastNameCondition = condition;
                if (condition.matches("\\d+")) {
                    throw new Exception("!!!Exception!!!");
                }
                List<Map<String, Object>> someData_ = new ArrayList<>();
                for (Map<String, Object> i : someData) {
                    if (i.get("lastName") != null) {
                        if (!i.get("lastName").toString().equals(lastNameCondition)) {
                            someData_.add(i);
                        }
                    }
                    someData = someData_;
                }
            } else if (condition.matches(".*like.*") && !condition.matches(".*ilike.*")) {
                condition = condition.replaceAll("(?i)like", "");
                condition = condition.replaceAll("'", "");
                condition = condition.replaceAll("%", "");
                String lastNameCondition = condition;
                if (condition.matches("\\d+")) {
                    throw new Exception("!!!Exception!!!");
                }
                List<Map<String, Object>> someData_ = new ArrayList<>();
                for (Map<String, Object> i : someData) {
                    if (i.get("lastName") != null) {
                        String currentLastName = i.get("lastName").toString();
                        if (currentLastName.matches(".*" + lastNameCondition + ".*")) {
                            someData_.add(i);
                        }
                    }
                    someData = someData_;
                }
            } else if (condition.matches(".*ilike.*")) {
                condition = condition.replaceAll("(?i)ilike", "");
                condition = condition.replaceAll("'", "");
                condition = condition.replaceAll("%", "");
                String lastNameCondition = condition.toLowerCase();
                if (condition.matches("\\d+")) {
                    throw new Exception("!!!Exception!!!");
                }
                List<Map<String, Object>> someData_ = new ArrayList<>();
                for (Map<String, Object> i : someData) {
                    if (i.get("lastName") != null) {
                        String currentLastName = i.get("lastName").toString().toLowerCase();
                        if (currentLastName.matches(".*" + lastNameCondition + ".*")) {
                            someData_.add(i);
                        }
                    }
                    someData = someData_;
                }
            } else if (condition.matches(".*>.*")) {
                throw new Exception("!!!Exception!!!");
            } else if (condition.matches(".*>.*") && condition.matches(".*=.*")) {
                throw new Exception("!!!Exception!!!");
            } else if (condition.matches(".*<.*")) {
                throw new Exception("!!!Exception!!!");
            } else if (condition.matches(".*<.*") && condition.matches(".*=.*")) {
                throw new Exception("!!!Exception!!!");
            }
            //age
        } else if (condition.matches("(?i).*age.*")) {
            condition = condition.replaceAll("(?i)'age'", "");
            if (condition.matches(".*>.*") && !condition.matches(".*=.*")) {
                condition = condition.replaceAll("(?i)>", "");
                try {
                    long ageCondition = Long.parseLong(condition);
                    List<Map<String, Object>> someData_ = new ArrayList<>();
                    for (Map<String, Object> i : someData) {
                        if (i.get("age") != null) {
                            if (Long.parseLong(i.get("age").toString()) > ageCondition) {
                                someData_.add(i);
                            }
                        }
                        someData = someData_;
                    }
                } catch (Exception e) {
                    throw new Exception("!!!Exception!!!");
                }

            } else if (condition.matches(".*<.*") && !condition.matches(".*=.*")) {
                condition = condition.replaceAll("(?i)<", "");
                try {
                    long ageCondition = Long.parseLong(condition);
                    List<Map<String, Object>> someData_ = new ArrayList<>();
                    for (Map<String, Object> i : someData) {
                        if (i.get("age") != null) {
                            if (Long.parseLong(i.get("age").toString()) < ageCondition) {
                                someData_.add(i);
                            }
                        }
                        someData = someData_;
                    }
                } catch (Exception e) {
                    throw new Exception("!!!Exception!!!");
                }

            } else if (condition.matches(".*=.*") &&
                    !condition.matches(".*>.*") &&
                    !condition.matches(".*<.*") &&
                    !condition.matches(".*!.*")) {
                condition = condition.replaceAll("(?i)=", "");
                try {
                    long ageCondition = Long.parseLong(condition);
                    List<Map<String, Object>> someData_ = new ArrayList<>();
                    for (Map<String, Object> i : someData) {
                        if (i.get("age") != null) {
                            if (Long.parseLong(i.get("age").toString()) == ageCondition) {
                                someData_.add(i);
                            }
                        }
                        someData = someData_;
                    }
                } catch (Exception e) {
                    throw new Exception("!!!Exception!!!");
                }

            } else if (condition.matches(".*!.*") && condition.matches(".*=.*")) {
                condition = condition.replaceAll("(?i)!=", "");
                try {
                    long ageCondition = Long.parseLong(condition);
                    List<Map<String, Object>> someData_ = new ArrayList<>();
                    for (Map<String, Object> i : someData) {
                        if (i.get("age") != null) {
                            if (Long.parseLong(i.get("age").toString()) != ageCondition) {
                                someData_.add(i);
                            }
                        }
                        someData = someData_;
                    }
                } catch (Exception e) {
                    throw new Exception("!!!Exception!!!");
                }

            } else if (condition.matches(".*<.*") && condition.matches(".*=.*")) {
                condition = condition.replaceAll("(?i)<=", "");
                try {
                    long ageCondition = Long.parseLong(condition);
                    List<Map<String, Object>> someData_ = new ArrayList<>();
                    for (Map<String, Object> i : someData) {
                        if (i.get("age") != null) {
                            if (Long.parseLong(i.get("age").toString()) <= ageCondition) {
                                someData_.add(i);
                            }
                        }
                        someData = someData_;
                    }
                } catch (Exception e) {
                    throw new Exception("!!!Exception!!!");
                }

            } else if (condition.matches(".*>.*") && condition.matches(".*=.*")) {
                condition = condition.replaceAll("(?i)>=", "");
                try {
                    long ageCondition = Long.parseLong(condition);
                    List<Map<String, Object>> someData_ = new ArrayList<>();
                    for (Map<String, Object> i : someData) {
                        if (i.get("age") != null) {
                            if (Long.parseLong(i.get("age").toString()) >= ageCondition) {
                                someData_.add(i);
                            }
                        }
                        someData = someData_;
                    }
                } catch (Exception e) {
                    throw new Exception("!!!Exception!!!");
                }

            }
            //cost
        } else if (condition.matches("(?i).*cost.*")) {
            condition = condition.replaceAll("(?i)'cost'", "");
            if (condition.matches(".*>.*") && !condition.matches(".*=.*")) {
                condition = condition.replaceAll("(?i)>", "");
                try {
                    double costCondition = Double.parseDouble(condition);
                    List<Map<String, Object>> someData_ = new ArrayList<>();
                    for (Map<String, Object> i : someData) {
                        if (i.get("cost") != null) {
                            if (Double.parseDouble(i.get("cost").toString()) > costCondition) {
                                someData_.add(i);
                            }
                        }
                        someData = someData_;
                    }
                } catch (Exception e) {
                    throw new Exception("!!!Exception!!!");
                }

            } else if (condition.matches(".*<.*") && !condition.matches(".*=.*")) {
                condition = condition.replaceAll("(?i)<", "");
                try {
                    double costCondition = Double.parseDouble(condition);
                    List<Map<String, Object>> someData_ = new ArrayList<>();
                    for (Map<String, Object> i : someData) {
                        if (i.get("cost") != null) {
                            if (Double.parseDouble(i.get("cost").toString()) < costCondition) {
                                someData_.add(i);
                            }
                        }
                        someData = someData_;
                    }
                } catch (Exception e) {
                    throw new Exception("!!!Exception!!!");
                }

            } else if (condition.matches(".*=.*") &&
                    !condition.matches(".*>.*") &&
                    !condition.matches(".*<.*") &&
                    !condition.matches(".*!.*")) {
                condition = condition.replaceAll("(?i)=", "");
                try {
                    double costCondition = Double.parseDouble(condition);
                    List<Map<String, Object>> someData_ = new ArrayList<>();
                    for (Map<String, Object> i : someData) {
                        if (i.get("cost") != null) {
                            if (Double.parseDouble(i.get("cost").toString()) == costCondition) {
                                someData_.add(i);
                            }
                        }
                        someData = someData_;
                    }
                } catch (Exception e) {
                    throw new Exception("!!!Exception!!!");
                }

            } else if (condition.matches(".*!.*") && condition.matches(".*=.*")) {
                condition = condition.replaceAll("(?i)!=", "");
                try {
                    double costCondition = Double.parseDouble(condition);
                    List<Map<String, Object>> someData_ = new ArrayList<>();
                    for (Map<String, Object> i : someData) {
                        if (i.get("cost") != null) {
                            if (Double.parseDouble(i.get("cost").toString()) != costCondition) {
                                someData_.add(i);
                            }
                        }
                        someData = someData_;
                    }
                } catch (Exception e) {
                    throw new Exception("!!!Exception!!!");
                }

            } else if (condition.matches(".*<.*") && condition.matches(".*=.*")) {
                condition = condition.replaceAll("(?i)<=", "");
                try {
                    double costCondition = Double.parseDouble(condition);
                    List<Map<String, Object>> someData_ = new ArrayList<>();
                    for (Map<String, Object> i : someData) {
                        if (i.get("cost") != null) {
                            if (Double.parseDouble(i.get("cost").toString()) <= costCondition) {
                                someData_.add(i);
                            }
                        }
                        someData = someData_;
                    }
                } catch (Exception e) {
                    throw new Exception("!!!Exception!!!");
                }


            } else if (condition.matches(".*>.*") && condition.matches(".*=.*")) {
                condition = condition.replaceAll("(?i)>=", "");
                try {
                    double costCondition = Double.parseDouble(condition);
                    List<Map<String, Object>> someData_ = new ArrayList<>();
                    for (Map<String, Object> i : someData) {
                        if (i.get("cost") != null) {
                            if (Double.parseDouble(i.get("cost").toString()) >= costCondition) {
                                someData_.add(i);
                            }
                        }
                        someData = someData_;
                    }
                } catch (Exception e) {
                    throw new Exception("!!!Exception!!!");
                }

            }
            //active
        } else if (condition.matches("(?i).*active.*")) {
            condition = condition.replaceAll("(?i)'active'", "");
            if (condition.matches(".*=.*") && !condition.matches(".*!.*")) {
                condition = condition.replaceAll("(?i)=", "");
                try {
                    boolean activeCondition = Boolean.parseBoolean(condition);
                    List<Map<String, Object>> someData_ = new ArrayList<>();
                    for (Map<String, Object> i : data) {
                        if (i.get("active") != null) {
                            if (Boolean.parseBoolean(i.get("active").toString()) == activeCondition) {
                                someData_.add(i);
                            }
                        }
                        someData = someData_;
                    }
                } catch (Exception e) {
                    throw new Exception("!!!Exception!!!");
                }

            } else if (condition.matches(".*!.*") && condition.matches(".*=.*")) {
                condition = condition.replaceAll("(?i)!=", "");
                try {
                    boolean activeCondition = Boolean.parseBoolean(condition);
                    List<Map<String, Object>> someData_ = new ArrayList<>();
                    for (Map<String, Object> i : data) {
                        if (i.get("active") != null) {
                            if (Boolean.parseBoolean(i.get("active").toString()) != activeCondition) {
                                someData_.add(i);
                            }
                        }
                        someData = someData_;
                    }
                } catch (Exception e) {
                    throw new Exception("!!!Exception!!!");
                }

            }
        }
        return someData;
    }


    //SELECT
    private List<Map<String, Object>> select(List<Map<String, Object>> data, List<String> reqArr) throws Exception {


        List<Map<String, Object>> selectedData = new ArrayList<>();

        String reqArrS = reqArr.toString();
        String reqArrSLC = reqArrS.toLowerCase();
        if (reqArrSLC.equals("[select]") ||
                reqArrSLC.equals("[select*]") ||
                reqArrSLC.equals("[select, *]")) {
            selectedData.addAll(data);
        }
        if (reqArrS.matches("(?i).*where.*")) {
            reqArr.remove(0);
            reqArr.remove(0);
            List<String> conditions = new ArrayList<>();

            if (!reqArrS.matches("(?i).*and.*") && !reqArrS.matches("(?i).*or.*")) {
                String lastCondition = reqArr.get(reqArr.size() - 1);
                reqArr.add("or");
                reqArr.add(lastCondition);
                reqArrS = reqArr.toString();
            }


            int[] or_and = {};
            List<String> or_andS = new ArrayList<>();
            if (reqArrS.matches("(?i).*and.*") || reqArrS.matches("(?i).*or.*")) {
                for (int i = 0; i < reqArr.size(); i++) {
                    if (reqArr.get(i).matches("(?i).*and.*") || reqArr.get(i).matches("(?i).*or.*")) {
                        or_and = Arrays.copyOf(or_and, or_and.length + 1);
                        or_and[or_and.length - 1] = i;
                        or_andS.add(reqArr.get(i));
                    }
                }
            }

            StringBuilder firsCondition = new StringBuilder();
            for (int i = 0; i < or_and[0]; i++) {
                firsCondition.append(reqArr.get(i));
            }
            conditions.add(firsCondition.toString());
            for (int i = 0; i < or_and.length; i++) {
                conditions.add(or_andS.get(i));
                StringBuilder condition = new StringBuilder();
                if (or_and[or_and.length - 1] == or_and[i]) {
                    condition.append(reqArr.get(or_and[i] + 1));
                    for (int j = or_and[i] + 2; j < reqArr.size(); j++) {
                        condition.append(reqArr.get(j));
                    }
                    conditions.add(condition.toString());
                } else {
                    condition.append(reqArr.get(or_and[i] + 1));
                    for (int k = or_and[i] + 2; k < or_and[i + 1]; k++) {
                        condition.append(reqArr.get(k));
                    }
                    conditions.add(condition.toString());
                }
            }


            List<Map<String, Object>> dataCondition = new ArrayList<>(conditionAnd(data, conditions.get(0)));
            String and_or_or = "";
            for (int i = 1; i < conditions.size(); i++) {
                if (conditions.get(i).matches("(?i).*or.*")) {
                    and_or_or = "or";
                } else if (conditions.get(i).matches("(?i).*and.*")) {
                    and_or_or = "and";
                } else if (!conditions.get(i).matches("(?i).*or.*") && !conditions.get(i).matches("(?i).*and.*")) {
                    if (and_or_or.equals("or")) {
                        selectedData.addAll(dataCondition);
                        dataCondition.clear();
                        dataCondition.addAll(conditionAnd(data, conditions.get(i)));
                    } else if (and_or_or.equals("and")) {
                        dataCondition = conditionAnd(dataCondition, conditions.get(i));
                    }
                }
            }
            selectedData.addAll(dataCondition);
            Set<Map<String, Object>> selectedSet = new HashSet<>(selectedData);
            selectedData.clear();
            selectedData.addAll(selectedSet);
        }
        return selectedData;
    }


    public List<Map<String, Object>> execute(String request) throws Exception {

        String[] requestArr = request.split(" ");
        List<String> reqArr = new ArrayList<>(Arrays.asList(requestArr));
        reqArr.removeIf(i -> i.equals(""));


        if (request.matches("(?i).*insert.*")) {
            insert(data, reqArr);
        } else if (request.matches("(?i).*update.*")) {
            update(reqArr);
        } else if (request.matches("(?i).*delete.*")) {
            delete(reqArr);
        } else if (request.matches("(?i).*select.*")) {
            List<Map<String, Object>> selectedData = new ArrayList<>(select(data, reqArr));
            System.out.println(selectedData);
        }


        return new ArrayList<>(data);
    }
}
