package com.hubosm.turingsimulator.dtos;

import com.hubosm.turingsimulator.domain.SimulationNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class NonDetSimulationDto {
    Map<Integer, SimulationNode> nodes = new HashMap<>();
}
