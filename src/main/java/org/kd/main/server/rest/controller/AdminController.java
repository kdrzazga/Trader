package org.kd.main.server.rest.controller;

import org.kd.main.server.TraderServer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @PostMapping(path = "/stop")
    public void stop() {
        TraderServer.stop();
    }
}