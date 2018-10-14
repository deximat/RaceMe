package com.codlex.raceme.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Server {
    String address;
    int port;
    public static final Server NONE = null;
}
