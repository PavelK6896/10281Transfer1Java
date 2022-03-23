package app.web.pavelk.transfer1;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Info {

    String id;
    String name;
    String pathOld;
    String pathCurrent;
    String self;

}
