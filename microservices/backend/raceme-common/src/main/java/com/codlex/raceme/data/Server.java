package com.codlex.raceme.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Server {
    String address;
    int port;
    public static final Server NONE = null;
}
