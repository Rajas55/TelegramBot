package Rajas.com.botRest.BotRest.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Entity
@Table(name="hsn_code")
public class HSNCode {

    @Id
    private int hsnCode;

    private String description;

    private float cGst;

    private float sGst;

    //
    @OneToMany(mappedBy = "hsnCode")
    private List<ProductModel> products =new ArrayList<ProductModel>();
}
