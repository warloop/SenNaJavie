package org.example.forum.repositories;

import org.example.forum.dto.Section.SectionAddDto;
import org.example.forum.dto.Subject.SubjectAddDto;
import org.example.forum.entities.Sections;
import org.example.forum.exception.DataAccessException;
import org.example.forum.repositories.Interfaces.ISectionRepository;
import org.example.forum.services.interfaces.IActionService;
import org.example.forum.util.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class SectionRepository implements ISectionRepository {
    @Autowired
    private IActionService ACTION_SERVICE;

    public Optional<Sections> getSectionById(long sectionId)
    {
        final String SQL = "SELECT * FROM sections WHERE id = ? AND is_banned = 0 AND is_deleted = 0";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {
            statement.setLong(1, sectionId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);

                    if(count > 0)
                    {
                        Sections section = new Sections();

                        section.setId(rs.getLong("id"));
                        section.setArticle_id(rs.getLong("article_id"));
                        section.setUser_adder_id(rs.getInt("user_adder_id"));
                        section.setSection_text(rs.getString("section_text"));
                        section.set_visible(rs.getBoolean("is_visible"));
                        section.set_banned(rs.getBoolean("is_banned"));
                        section.set_deleted(rs.getBoolean("is_deleted"));

                        return Optional.of(section);
                    }


                    return Optional.of(null);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
        return Optional.of(null);
    }

    @Override
    public Optional<Long> addSection(Sections section) {
        final String SQL = "INSERT INTO sections (user_adder_id, article_id, section_text) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, section.getUser_adder_id());
            statement.setLong(2, section.getArticle_id());
            statement.setString(3, section.getSection_text());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                return Optional.empty();
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return Optional.of(generatedKeys.getLong(1));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean editSection(Sections section) {
        final String SQL = "UPDATE sections SET user_adder_id = ?, article_id = ?, section_text = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {

            statement.setInt(1, section.getUser_adder_id());
            statement.setLong(2, section.getArticle_id());
            statement.setString(3, section.getSection_text());
            statement.setLong(4, section.getId());

            int affectedRows = statement.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<Sections> getAllSectionsByArticleId(long articleId) {
        List<Sections> sections = new ArrayList<>();
        final String SQL = "SELECT * FROM sections WHERE article_id = ? AND is_banned = 0 AND is_deleted = 0";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {
            statement.setLong(1, articleId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Sections section = new Sections();
                    section.setId(rs.getLong("id"));
                    section.setArticle_id(rs.getLong("article_id"));
                    section.setUser_adder_id(rs.getInt("user_adder_id"));
                    section.setSection_text(rs.getString("section_text"));
                    section.set_visible(rs.getBoolean("is_visible"));
                    section.set_banned(rs.getBoolean("is_banned"));
                    section.set_deleted(rs.getBoolean("is_deleted"));

                    sections.add(section);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }

        return sections;
    }
}
