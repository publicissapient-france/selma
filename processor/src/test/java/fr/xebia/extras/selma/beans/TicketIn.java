/*
 * Copyright 2013  Séven Le Mesle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package fr.xebia.extras.selma.beans;

/**
 * Created by slemesle on 17/10/2014.
 */
public class TicketIn {

    private String login;
    private String password;

    private TicketTocken tocken;

    public TicketIn() {
    }

    public TicketIn(String login, String password, TicketTocken tocken) {
        this.login = login;
        this.password = password;
        this.tocken = tocken;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TicketTocken getTocken() {
        return tocken;
    }

    public void setTocken(TicketTocken tocken) {
        this.tocken = tocken;
    }
}
