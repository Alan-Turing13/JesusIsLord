package com.dominicjmarshall.springbootday1.config;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.dominicjmarshall.springbootday1.models.Account;
import com.dominicjmarshall.springbootday1.models.Authority;
import com.dominicjmarshall.springbootday1.models.Post;
import com.dominicjmarshall.springbootday1.service.AccountService;
import com.dominicjmarshall.springbootday1.service.AuthorityService;
import com.dominicjmarshall.springbootday1.service.PostService;
import com.dominicjmarshall.springbootday1.util.constants.Privileges;
import com.dominicjmarshall.springbootday1.util.constants.Roles;

@Component
public class SeedData implements CommandLineRunner{

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityService authorityService;

    @Override
    public void run(String... args) throws Exception {

        for(Privileges auth: Privileges.values()){
            Authority authority = new Authority();
            authority.setId(auth.getId());
            authority.setName(auth.getName());
            authorityService.save(authority);
        }
        
        Account account01 = new Account();
        Account account02 = new Account();
        Account account03 = new Account();
        Account account04 = new Account();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        account01.setEmail("account01@gmail.com");
        account01.setPassword("12345678910");
        account01.setFirstName("Herman");
        account01.setLastName("Melville");
        account01.setGender("Male");
        account01.setAge(34);
        account01.setBirthday(LocalDate.parse("13-04-1989", dateFormat));     

        account02.setEmail("mavenoshea@gmail.com");
        account02.setPassword("datawipe");
        account02.setFirstName("Maven");
        account02.setLastName("O'Shea");
        account02.setGender("Female");
        account02.setAge(44);
        account02.setBirthday(LocalDate.parse("29-01-1980", dateFormat));     
        account02.setRole(Roles.ADMIN.getRole());

        account03.setEmail("imanshumpert@gmail.com");
        account03.setPassword("perpendicular");
        account03.setFirstName("Iman");
        account03.setLastName("Shumpert");
        account03.setGender("Male");
        account03.setAge(27);
        account03.setBirthday(LocalDate.parse("29-01-1997", dateFormat)); 
        account03.setRole(Roles.EDITOR.getRole());

        account04.setEmail("cruftsontrumpet@gmile.com");
        account04.setPassword("success");
        account04.setFirstName("Rod");
        account04.setLastName("Love");
        account04.setGender("Male");
        account04.setAge(51);
        account04.setBirthday(LocalDate.parse("29-01-1973", dateFormat));         
        account04.setRole(Roles.EDITOR.getRole());
        Set<Authority> authorities = new HashSet<>();
        authorityService.findById(Privileges.RESET_ANY_USER_PASSWORD.getId()).ifPresent(authorities::add);
        authorityService.findById(Privileges.ACCESS_ADMIN_PANEL.getId()).ifPresent(authorities::add);;
        account04.setAuthorities(authorities);

        accountService.save(account01);
        accountService.save(account02);
        accountService.save(account03);
        accountService.save(account04);

        List<Post> posts = postService.getAll(); 
        if (posts.size() == 0) {
            Post post01 = new Post();
            post01.setTitle("Prayer for the Day");
            post01.setBody("God our creator,\n" + //
                "who in the beginning\n" + //
                "commanded the light to shine out of darkness:\n" + //
                "we pray that the light of the glorious gospel of Christ\n" + //
                "may dispel the darkness of ignorance and unbelief,\n" + //
                "shine into the hearts of all your people,\n" + //
                "and reveal the knowledge of your glory\n" + //
                "in the face of Jesus Christ your Son our Lord,\n" + //
                "who is alive and reigns with you,\n" + //
                "in the unity of the Holy Spirit,\n" + //
                "one God, now and for ever.\n" + //
                "Amen.");
            post01.setAccount(account01);
            postService.save(post01);

            Post post02 = new Post();
            post02.setTitle("Psalm 67");
            post02.setBody("God be gracious to us and bless us \n" + //
                "   and make his face to shine upon us,\n" + //
                "\n" + //
                "That your way may be known upon earth, \n" + //
                "   your saving power among all nations.\n" + //
                "\n" + //
                "Let the peoples praise you, O God; \n" + //
                "   let all the peoples praise you.\n" + //
                "\n" + //
                "O let the nations rejoice and be glad, \n" + //
                "   for you will judge the peoples righteously\n" + //
                "      and govern the nations upon earth.\n" + //
                "\n" + //
                "Let the peoples praise you, O God; \n" + //
                "   let all the peoples praise you.\n" + //
                "\n" + //
                "Then shall the earth bring forth her increase, \n" + //
                "   and God, our own God, will bless us.\n" + //
                "\n" + //
                "God will bless us, \n" + //
                "   and all the ends of the earth shall fear him.\n" + //
                "\n" + //
                "In the face of Jesus Christ\n" + //
                "your light and glory have blazed forth,\n" + //
                "O God of all the nations;\n" + //
                "with all your people,\n" + //
                "may we make known your grace\n" + //
                "and walk in the ways of peace;\n" + //
                "for your name’s sake.");
            post02.setAccount(account02);
            postService.save(post02);

            Post post03 = new Post();
            post03.setTitle("Morning Prayer");
            post03.setBody("""
                O be joyful in the Lord, all the earth; 
                serve the Lord with gladness
                and come before his presence with a song.
                
                Know that the Lord is God; 
                it is he that has made us and we are his;
                we are his people and the sheep of his pasture.
                
                Enter his gates with thanksgiving
                and his courts with praise; 
                give thanks to him and bless his name.
                
                For the Lord is gracious; his steadfast love is everlasting, 
                and his faithfulness endures from generation to generation.
            """);
            post03.setAccount(account01);
            postService.save(post03);

            Post post04 = new Post();
            post04.setTitle("Psalm 99");
            post04.setBody("""
                The Lord is king: let the peoples tremble; 
                he is enthroned above the cherubim: let the earth shake.

                The Lord is great in Zion 
                and high above all peoples.

                Let them praise your name, which is great and awesome; 
                the Lord our God is holy.

                Mighty king, who loves justice,
                you have established equity; 
                you have executed justice and righteousness in Jacob.

                Exalt the Lord our God; 
                bow down before his footstool, for he is holy.

                Moses and Aaron among his priests
                and Samuel among those who call upon his name, 
                they called upon the Lord and he answered them.

                He spoke to them out of the pillar of cloud; 
                they kept his testimonies and the law that he gave them.

                You answered them, O Lord our God; 
                you were a God who forgave them
                and pardoned them for their offences.

                Exalt the Lord our God
                and worship him upon his holy hill, 
                for the Lord our God is holy.

                Lord God, mighty king,
                you love justice and establish equity;
                may we love justice more than gain
                and mercy more than power;
                through Jesus Christ our Lord.
            """);
            post04.setAccount(account03);
            postService.save(post04);

            Post post05 = new Post();
            post05.setTitle("Psalm 110");
            post05.setBody("""
                The Lord is king and has put on glorious apparel.

                The Lord said to my lord, ‘Sit at my right hand,
                until I make your enemies your footstool.’

                May the Lord stretch forth the sceptre of your power;
                rule from Zion in the midst of your enemies.

                ‘Noble are you on this day of your birth;
                on the holy mountain, from the womb of the dawn
                the dew of your new birth is upon you.’ 

                The Lord has sworn and will not retract:
                ‘You are a priest for ever after the order of Melchizedek.’

                The king at your right hand, O Lord,
                shall smite down kings in the day of his wrath. 

                In all his majesty, he shall judge among the nations,
                smiting heads over all the wide earth.

                He shall drink from the brook beside the way;
                therefore shall he lift high his head.

                The Lord is king and has put on glorious apparel.

                Lord Jesus, divine Son and eternal priest,
                inspire us with the confidence of your final conquest of evil,
                and grant that daily on our way
                we may drink of the brook of your eternal life
                and so find courage against all adversities;
                for your mercy’s sake.    
            """);
            post05.setAccount(account04);
            postService.save(post05);

            Post post06 = new Post();
            post06.setTitle("Genesis 21:1-21");
            post06.setBody("""
                The Lord dealt with Sarah as he had said, and the Lord did for Sarah as he had promised. Sarah conceived and bore Abraham a son in his old age, at the time of which God had spoken to him. Abraham gave the name Isaac to his son whom Sarah bore him. And Abraham circumcised his son Isaac when he was eight days old, as God had commanded him. Abraham was a hundred years old when his son Isaac was born to him. Now Sarah said, ‘God has brought laughter for me; everyone who hears will laugh with me.’ And she said, ‘Who would ever have said to Abraham that Sarah would nurse children? Yet I have borne him a son in his old age.’
                The child grew, and was weaned; and Abraham made a great feast on the day that Isaac was weaned. But Sarah saw the son of Hagar the Egyptian, whom she had borne to Abraham, playing with her son Isaac. So she said to Abraham, ‘Cast out this slave woman with her son; for the son of this slave woman shall not inherit along with my son Isaac.’ The matter was very distressing to Abraham on account of his son. But God said to Abraham, ‘Do not be distressed because of the boy and because of your slave woman; whatever Sarah says to you, do as she tells you, for it is through Isaac that offspring shall be named after you. As for the son of the slave woman, I will make a nation of him also, because he is your offspring.’ So Abraham rose early in the morning, and took bread and a skin of water, and gave it to Hagar, putting it on her shoulder, along with the child, and sent her away. And she departed, and wandered about in the wilderness of Beer-sheba.

                When the water in the skin was gone, she cast the child under one of the bushes. Then she went and sat down opposite him a good way off, about the distance of a bowshot; for she said, ‘Do not let me look on the death of the child.’ And as she sat opposite him, she lifted up her voice and wept. And God heard the voice of the boy; and the angel of God called to Hagar from heaven, and said to her, ‘What troubles you, Hagar? Do not be afraid; for God has heard the voice of the boy where he is. Come, lift up the boy and hold him fast with your hand, for I will make a great nation of him.’ Then God opened her eyes, and she saw a well of water. She went, and filled the skin with water, and gave the boy a drink.

                God was with the boy, and he grew up; he lived in the wilderness, and became an expert with the bow. He lived in the wilderness of Paran; and his mother got a wife for him from the land of Egypt.  
            """);
            post06.setAccount(account01);
            postService.save(post06);

        }
    }
    
}
