package net.tisseurdetoile.batch.socle.vo;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Region {
    private String regionId;
    private String chefLieuCp;
    private String tncc;
    private String ncc;
    private String nccenr;
}
